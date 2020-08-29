# harver.py, connect sql.py 모두 합친 것
import sys
import mysql.connector
import json
import requests
from scipy.spatial.distance import pdist, squareform
from sklearn.cluster import DBSCAN
from haversine import haversine

# ChildKey 얻어오기
argvList = sys.argv

# mysql 접속에 필요한 ID, PW, DBName, Port
config = {
    "user": "IMCP",
    "password": "Security915!",
    "host": "localhost",
    "database": "IMCP",
    "port": "3307",
    "charset": "utf8"
}


# connect
def connectionDB(sql, param):
    conn = mysql.connector.connect(**config)
    curs = conn.cursor()

    curs.execute(sql, param)

    rows = curs.fetchall()
    curs.close()
    return rows


# 아이 현재 위치 구하기
def getNowChildGPS(childKey):
    sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 1"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)

    # gps_info[1]: Time
    # gps_info[2]: Latitude
    # gps_info[3]: Longitude
    return result


# 부모 아이디 구하기
def getParentID(childKey):
    sql = "select ID from PtoC where ChildKey = %s"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)
    return result


# 부모 위치 구하기
def getParentGPS(ID):
    sql = "select * from PARENT_GPS where ID = %s"
    reID = (ID,)
    result = connectionDB(sql, reID)
    return result


# 초기 안전 위치 구하기
def getInitialGPS(childKey):
    sql = "select * from CHILD_GPS_INITIAL where ChildKey = %s"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)
    return result


# 초기 안전위치와 아이 현재위치 비교
def compareInitial(childLocation, initialData):
    count = len(initialData)
    for i in initialData:
        inidata = list(i)
        iniLocation = inidata[1], inidata[2]
        interval = haversine(childLocation, iniLocation)
        if interval <= 0.05:
            break
        count -= 1

    if count == 0:
        return False
    else:
        return True


# 아이 빅데이터 구하기
def getChildData(childKey):
    sql = "select * from CHILD_DATA where ChildKey = %s"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)
    return result


# 노이즈 제거(DB SCAN 이용)
def doDbscan(df):
    df.columns = ['ChildKey', 'Time', 'latitude', 'longitude']
    coord = df[['latitude', 'longitude']]

    #dbscan with habersine method
    distance_matrix = squareform(pdist(coord, (lambda u, v: haversine(u, v))))
    db = DBSCAN(eps=0.5, min_samples=3, metric='precomputed')
    y_db = db.fit_predict(distance_matrix)

    #Cluster's Info - '-1' is Noise Cluster in X['Cluster'] data
    coord['cluster'] = y_db

    # Drop All of Noise Data
    for i, row in coord.iterrows():
        if row['cluster'] == -1:
            coord.drop(i, inplace=True)

    return coord


# 아이 현재 위치와 빅데이터 비교
def compareData(childKey, location):
    data = getChildData(childKey)
    cleansingData = doDbscan(data)

    for i in cleansingData:
        interval = haversine(location, i)
        if interval < 0.5:
            return True

    return False


# 영역밖 확인
def outCheck(childKey):
    sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 30"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)
    count = 0
    # 영역밖 30번 카운트
    for i in result:
        location = (i[2], i[3])
        if compareData(childKey, location):
            break
        count += 1

    if count == 30:
        return True
    else:
        return False


# 같은 위치 30번 체크
def similarLocationCheck(childKey):
    sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 30"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)
    nowGPS = getNowChildGPS(childKey)
    nowLocation = (nowGPS[2], nowGPS[3])
    count = 0
    for i in result:
        location = (i[2], i[3])
        interval = haversine(nowLocation, location)
        if interval > 0.05:
            break
        count += 1

    if count == 30:
        return True
    else:
        return False


# 부모 토큰값 얻기
def getParentToken(childKey):
    ID = getParentID(childKey)
    sql = "select Token from PARENT_TOKEN where ID = %s or ID = %s"
    result = connectionDB(sql, ID)
    return result


# FCM 보내기
def sendFCM(childKey):
    server_key='AAAAK-E7ezg:APA91bHMDCXaStMIhwELOcDylGkg-W8EuUPfl8Jt3d2T5B0kdp_o8-IxLvuf9zeCu_vV8KEbLn9Lu6C9XrAwE8ezlJR2kgcrgAz2G7LSgtYY8Gn24r85qR1zE3zno-xdJlhvdYAwY9sK'
    tupleToken = getParentToken(childKey)
    token = []
    for i in tupleToken:
        token.append(i[0])
    headers = {
        'Authorization': 'key= ' + server_key,
        'Content-Type': 'application/json; UTF-8'
    }

    data = {
        'registration_ids': token,
        'data': {
            'title': 'IMCP',
            'body': '아이가 위험해요',
            'childKey': childKey,
            'danger': 'on'
        }
    }

    response = requests.post('https://fcm.googleapis.com/fcm/send', headers=headers, data=json.dumps(data))
    print(response)


child = argvList[1]
childGPS = getNowChildGPS(child)
childGPS = list(childGPS[0])
childLocation = childGPS[2], childGPS[3]

parent = getParentID(child)
parentGPS = getParentGPS(parent)
parentGPS = list(parentGPS[0])
parentLocation = parentGPS[2], parentGPS[3]

PtoC = haversine(childLocation, parentLocation)

if PtoC < 0.05:
    # 부모와 가까이 있을 경우
    exit()
elif compareInitial(childLocation, getInitialGPS(child)):
    # 부모가 설정한 초기 안전위치내에 있을 경우
    exit()
elif outCheck(child):
    sendFCM(child)
elif similarLocationCheck(child):
    sendFCM(child)
else:
    exit()
