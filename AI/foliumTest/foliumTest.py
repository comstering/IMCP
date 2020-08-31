import folium
import pandas as pd
from scipy.spatial.distance import pdist, squareform


#Test Data Set
'''
df1 = pd.read_csv('./GpsData/CHILD_GPS(한수1).csv')
df2 = pd.read_csv('./GpsData/CHILD_GPS(한수2).csv')
df3 = pd.read_csv('./GpsData/CHILD_GPS(김규1).csv')
df4 = pd.read_csv('./GpsData/CHILD_GPS(김규2).csv')
'''
df = pd.read_csv('./GpsData/CHILD_GPS(한수1).csv')

#Give Columns  Name
df.columns = ['ChildNum', 'Date', 'latitude', 'longitude']

#DataFrame Convert To List
geo = df[['latitude', 'longitude']].values.tolist()


#Folium Map Setting
map = folium.Map(
    location=[geo[0][0], geo[0][1]],
    zoom_start=20
)

#Make A Foliummap For Test
for lat, lon in geo:
    folium.Marker(location=[lat, lon],
        popup= str(lat) + ' and ' + str(lon),
        icon=folium.Icon(color='red', icon='star')).add_to(map)
map.save('./foliumTest/map.html')

