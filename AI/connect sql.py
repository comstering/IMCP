import mysql.connector

#mysql 접속에 필요한 ID, PW, DBName, Port
config = {
    "user":"IMCP",
    "password":"Security915!",
    "host":"localhost",
    "database":"IMCP",
    "port":"3307",
    "charset":"utf8"
}

#connect
conn = mysql.connector.connect(**config)
curs = conn.cursor()

sql = "select * from CHILD_GPS where ChildKey = %s order by Time desc limit 1"
curs.execute(sql, ('',))
rows = curs.fetchall()

#gps_info[0]: ChildKey
#gps_info[1]: Time
#gps_info[2]: Latitude
#gps_info[3]: Longitude
gps_info = list(rows[0])


curs.close()