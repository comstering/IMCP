from scipy.spatial.distance import pdist, squareform
from sklearn.cluster import DBSCAN
from haversine import haversine

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