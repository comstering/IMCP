# harver.py, connect sql.py 모두 합친 것
import sys
import mysql.connector

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
conn = mysql.connector.connect(**config)
curs = conn.cursor()

# 아이 현재위치 가져오기
sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 1"
curs.execute(sql, (argvList[1],))

# 결과 값 가져오기
rows = curs.fetchall()

# gps_info[0]: ChildKey
# gps_info[1]: Time
# gps_info[2]: Latitude
# gps_info[3]: Longitude
gps_info = list(rows[0])

curs.close()