# haver.py, connect sql.py, dbscanMethod.py 모두 합친 것
import sys
import mysql.connector
import json
import requests
import pandas as pd
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

conn = mysql.connector.connect(**config)


# connect
def connectionDB(sql, param):
    # DB 연결
    curs = conn.cursor()
    curs.execute(sql, param)

    # sql 결과 값 가져오기
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
def getParentGPS(parentID):
    sql = "select * from PARENT_GPS where ID = %s"

    # gpsList 부모 위치 정보 획득
    gpsList = []
    for ID in parentID:
        gpsList.append(connectionDB(sql, ID)[0])

    return gpsList


# 부모와 아이와의 거리 비교
def comparePrentChild(cLocation, pLocation):
    for location in pLocation:
        # 50미터 이하로 가까이 있을 경우
        if haversine(cLocation, location) < 0.05:
            return True

    return False


# 초기 안전 위치 구하기
def getInitialGPS(childKey):
    sql = "select * from CHILD_GPS_INITIAL where ChildKey = %s"
    reChildKey = (childKey,)
    result = connectionDB(sql, reChildKey)
    return result


# 초기 안전위치와 아이 현재위치 비교
def compareInitial(childLocation, initialData):
    count = len(initialData)
    for initial in initialData:
        iniLocation = initial[1], initial[2]
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
def doDbscan(data):
    # 튜플리스트를 데이터프레임으로 변환
    df = pd.DataFrame(data, columns=['childKey', 'Time', 'latitude', 'longitude'])
    # latitude와 longitude만 사용
    coord = df[['latitude', 'longitude']]

    # dbscan with haversine method
    distance_matrix = squareform(pdist(coord, (lambda u, v: haversine(u, v))))
    db = DBSCAN(eps=0.5, min_samples=3, metric='precomputed')
    y_db = db.fit_predict(distance_matrix)

    # Cluster's Info - '-1' is Noise Cluster in X['Cluster'] data
    coord = coord.assign(cluster=y_db)

    # Drop All of Noise Data
    for ite, row in coord.iterrows():
        if row['cluster'] == -1:
            coord.drop(ite, inplace=True)

    result = coord[['latitude', 'longitude']]
    dataSet = list(result.itertuples(index=False, name=None))

    return dataSet


# 아이 현재 위치와 빅데이터 비교
def compareData(childKey, location):
    data = getChildData(childKey)
    cleansingData = doDbscan(data)

    for data in cleansingData:
        interval = haversine(location, data)
        if interval < 0.5:
            return True

    return False


# 영역밖 확인
def outCheck(childKey):
    sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 30"
    reChildKey = (childKey,)
    countGPS = connectionDB(sql, reChildKey)
    count = 0
    # 영역밖 30번 카운트
    for gps in countGPS:
        location = (gps[2], gps[3])
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
    countGPS = connectionDB(sql, reChildKey)
    nowGPS = getNowChildGPS(childKey)
    nowLocation = (nowGPS[2], nowGPS[3])
    count = 0
    # 같은 위치 30번 카운트
    for gps in countGPS:
        location = (gps[2], gps[3])
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
    parentID = getParentID(childKey)
    sql = "select Token from PARENT_TOKEN where ID = %s"
    result = []
    for ID in parentID:
        result.append(connectionDB(sql, ID))
    return result


# FCM 보내기
def sendFCM(childKey):
    # FCM 서버키
    server_key = 'AAAAK-E7ezg:APA91bHMDCXaStMIhwELOcDylGkg-W8EuUPfl8Jt3d2T5B0kdp_o8-IxLvuf9zeCu_vV8KEbLn9Lu6C9XrAwE8ezlJR2kgcrgAz2G7LSgtYY8Gn24r85qR1zE3zno-xdJlhvdYAwY9sK'
    tupleToken = getParentToken(childKey)
    tokenList = []
    for token in tupleToken:
        tokenList.append(token[0])

    print(tokenList)
    headers = {
        'Authorization': 'key= ' + server_key,
        'Content-Type': 'application/json; UTF-8'
    }

    data = {
        'registration_ids': tokenList,
        'data': {
            'title': 'IMCP',
            'body': '아이가 위험해요',
            'childKey': childKey,
            'danger': 'on'
        }
    }

    # FCM 서버로 데이터 전송
    response = requests.post('https://fcm.googleapis.com/fcm/send', headers=headers, data=json.dumps(data))
    print(response)


# 아이 식별값 획득
child = argvList[1]
# 아이 현재위치 획득
childGPS = getNowChildGPS(child)
childGPS = childGPS[0]
# 아이 위치 location으로 저장
childLocation = childGPS[2], childGPS[3]
# 부모 현재 위치 획득
parent = getParentID(child)
parentGPS = getParentGPS(parent)

# 부모 위치 location으로 저장
parentLocation = []
for GPS in parentGPS:
    parentLocation.append((GPS[2], GPS[3]))

if comparePrentChild(childLocation, parentLocation) < 0.05:
    # 부모와 가까이 있을 경우
    exit()
elif compareInitial(childLocation, getInitialGPS(child)):
    # 부모가 설정한 초기 안전위치내에 있을 경우
    exit()
elif outCheck(child):
    # 경로이탈 30번 울렸을 경우
    sendFCM(child)
elif similarLocationCheck(child):
    # 같은 위치 30번일 경우
    sendFCM(child)
else:
    exit()
