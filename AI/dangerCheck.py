# harver.py, connect sql.py 모두 합친 것
import sys
import datetime
import mysql.connector
from haversine import haversine

# ChildKey 얻어오기
argvList = str(sys.argv)

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
    reChildKey = (childKey, )
    result = connectionDB(sql, reChildKey)

    # gps_info[1]: Time
    # gps_info[2]: Latitude
    # gps_info[3]: Longitude
    return result

# 부모 아이디 구하기
def getParentID(childKey):
    sql = "select ID from PtoC where ChildKey = %s"
    reChildKey = (childKey, )
    result = connectionDB(sql, reChildKey)
    return result

# 부모 위치 구하기
def getParentGPS(ID):
    sql = "select * from PARENT_GPS where ID = %s"
    result = connectionDB(sql, ID)
    return result

# 초기 안전 위치 구하기
def getInitialGPS(childKey):
    sql = "select * from CHILD_GPS_INITIAL where ChildKey = %s"
    reChildKey = (childKey, )
    result = connectionDB(sql, reChildKey)
    return result

# 요일별 시간별 나누기
def splittime(hour):
    # dayofseek(Time)
    # 1: 일요일
    # 2: 월요일
    # 3: 화요일
    # 4: 수요일
    # 5: 목요일
    # 6: 금요일
    # 7: 토요일
    # dayofseek(now()): 오늘에 해당하는 요일
    # hour(Time): 시간만 추출
    if 7 <= hour <= 9:
        return "dayofweek(Time) = dayofweek(now()) and hour(Time) >= 7 and hour(Time) <= 9"
    elif 10 <= hour <= 12:
        return "dayofweek(Time) = dayofweek(now()) and hour(Time) >= 10 and hour(Time) <= 12"
    elif 13 <= hour <= 15:
        return "dayofweek(Time) = dayofweek(now()) and hour(Time) >= 13 and hour(Time) <= 15"
    elif 16 <= hour <= 18:
        return "dayofweek(Time) = dayofweek(now()) and hour(Time) >= 16 and hour(Time) <= 18"
    elif 19 <= hour <= 21:
        return "dayofweek(Time) = dayofweek(now()) and hour(Time) >= 19 and hour(Time) <= 21"
    elif 22 <= hour <= 23:
        return "(dayofweek(Time) = dayofweek(now()) and hour(Time) >= 22) or (dayofweek(Time) = dayofweek(now()) + 1 " \
               "and hour(Time) <= 6) "
    else:
        return "(dayofweek(Time) = dayofweek(now()) - 1 and hour(Time) >= 22) or (dayofweek(Time) = dayofweek(now()) " \
               "and hour(Time) <= 6) "

# 아이 요일별 시간별 나눈 데이터 추출
def getChildGPSData(childKey):
    now = datetime.datetime.now()
    hour = now.hour
    subSQL = splittime(hour)

    sql = "select * from Child_GPS where ChildKey = %s and (%s) order by Time desc"
    param = (childKey, subSQL)
    result = connectionDB(sql, param)
    return result


"""
# gps_info[1]: Time
# gps_info[2]: Latitude
# gps_info[3]: Longitude
gps_info = list(rows[0])
"""
