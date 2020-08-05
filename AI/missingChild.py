import pandas as pd

df1 = pd.read_csv('./GpsDataa/CHILD_GPS(한수1).csv', header = None)
df2 = pd.read_csv('./GpsDataa/CHILD_GPS(한수2).csv')
df3 = pd.read_csv('./GpsDataa/CHILD_GPS(김규1).csv')
df4 = pd.read_csv('./GpsDataa/CHILD_GPS(김규2).csv')

df1.columnx = ['ChildCode', 'Date', 'latitude', 'longitude']

print(df1.head(5))