from scipy.spatial.distance import pdist, squareform
from sklearn.cluster import DBSCAN
from haversine import haversine

import matplotlib.pyplot as plt
import pandas as pd

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
X = df[['latitude', 'longitude']]

#DataFrame Convert To List
#geo = df3[['latitude', 'longitude']].values.tolist()

distance_matrix = squareform(pdist(X, (lambda u, v: haversine(u, v))))

db = DBSCAN(eps=0.5, min_samples=3, metric='precomputed')
y_db = db.fit_predict(distance_matrix)
X['cluster'] = y_db

#Cluster's Info
print(X['cluster'].drop_duplicates())

plt.scatter(X['longitude'], X['latitude'], c=X['cluster'])
plt.show()