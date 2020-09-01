from scipy.spatial.distance import pdist, squareform
from sklearn.cluster import DBSCAN
from haversine import haversine

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

#Test Data Set
'''
df1 = pd.read_csv('./GpsData/CHILD_GPS(한수1).csv')
df2 = pd.read_csv('./GpsData/CHILD_GPS(한수2).csv')
df3 = pd.read_csv('./GpsData/CHILD_GPS(김규1).csv')
df4 = pd.read_csv('./GpsData/CHILD_GPS(김규2).csv')
'''
df = pd.read_csv('./GpsData/CHILD_GPS(한수1).csv')

#Give Columns  Name
df.columns = ['ChildKey', 'Time', 'latitude', 'longitude']
X = df[['latitude', 'longitude']]


distance_matrix = squareform(pdist(X, (lambda u, v: haversine(u, v))))

db = DBSCAN(eps=0.45, min_samples=3, metric='precomputed')
y_db = db.fit_predict(distance_matrix)

#Cluster's Info - '-1' is Noise Cluster in X['Cluster'] data
X['cluster'] = y_db
'''
X['color'] = np.where(X.cluster == -1, 'red', 'blue')
X.plot(kind='scatter',
       x='longitude',
       y='latitude',
       s=20,
       c=X['color'])
'''
'''
#Drop All of Noise Data
for i, row in X.iterrows():
    if row['cluster'] == -1:
        X.drop(i, inplace=True)
'''
plt.scatter(X['longitude'], X['latitude'], c=X['cluster'], label=X['cluster'])
plt.title('DBSCAN')
plt.xlabel('longitude')
plt.ylabel('latitude')
plt.show()
