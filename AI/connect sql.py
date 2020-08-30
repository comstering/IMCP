import mysql.connector

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
conn = mysql.connector.connect(**config)
curs = conn.cursor()

# 현재 아이 위치 가져오기
sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 1"
curs.execute(sql, ('ChildKey',))
rows = curs.fetchall()

# gps_info[0]: ChildKey
# gps_info[1]: Time
# gps_info[2]: Latitude
# gps_info[3]: Longitude
gps_info = list(rows[0])

# 요일별 시간별 나누기
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
sql = "select * from Child_GPS where ChildKey = %s and dayofweek(Time) = dayofweek(now) and hour(Time) >= 7 and hour(Time) < 11 order by Time desc"
curs.execute(sql, ('ChildKey',))
rows = curs.fetchall()

curs.close()
