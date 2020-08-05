import pandas as pd
import shapefile
from pyproj import Proj, transform

shp_path_node = './[2020-03-31]NODELINKDATA/MOCT_NODE.shp'
sf_node = shapefile.Reader(shp_path_node, encoding='cp949')
shp_path_link = './[2020-03-31]NODELINKDATA/MOCT_LINK.shp'
sf_link = shapefile.Reader(shp_path_link, encoding='cp949')
fields_node = [x[0] for x in sf_node.fields][1:]

records_node = sf_node.records()
shps = [s.points for s in sf_node.shapes()] # node has coordinate data.
# link
fields_link = [x[0] for x in sf_link.fields][1:]
records_link = sf_link.records()

#write the records into a dataframe
#node
node_dataframe = pd.DataFrame(columns=fields_node, data=records_node)
#add the coordinate data to a column called "coords"
node_dataframe = node_dataframe.assign(coords=shps)
# link
link_dataframe = pd.DataFrame(columns=fields_link, data=records_link)

'''
서울 : 100 ~ 124
부산 : 130 ~ 145
대구 : 150 ~ 157
인천 : 161 ~ 170
광주 : 175 ~ 179
대전 : 183 ~ 187
울산 : 192 ~ 196
'''
print(node_dataframe)
# Data restriction
range_STNL_REG=range(183, 187) # STNL_REG for Daejeon
df_node = pd.DataFrame()
df_link = pd.DataFrame()
for ii in range_STNL_REG:
    res_node = node_dataframe[node_dataframe['STNL_REG'] == str(ii)] # STNL_REG is not int.
    res_link = link_dataframe[link_dataframe['STNL_REG'] == str(ii)]
    df_node = pd.concat([df_node,res_node],ignore_index=True) # marge data
    df_link = pd.concat([df_link,res_link],ignore_index=True)

# Change node name in korean
for idx,row in df_node.iterrows():
    if type(row['NODE_NAME']) == bytes:
        row['NODE_NAME'] = row['NODE_NAME'].decode('cp949')


# Change coordinate system
# korea 2000/central belt 2010 (epsg:5186) to wgs84(epsg:4326)
inProj = Proj(init = 'epsg:5186')
outProj= Proj(init = 'epsg:4326')
latitude = []
longitude= []
for idx, row in df_node.iterrows():
    x,y = row.coords[0][0],row.coords[0][1]  # korea 2000 좌표계
    nx,ny = transform(inProj,outProj,x,y)     # 새로운 좌표계
    latitude.append(ny)
    longitude.append(nx)
df_node['latitude'] = latitude
df_node['longitude']= longitude
del df_node['coords'] # delete coords

# Change column name to draw network in Gephi
df_node.rename(columns={'NODE_ID':'Id'},inplace = True)
df_link.rename(columns={'F_NODE':'Source','T_NODE':'Target'},inplace = True)

df_node.to_csv('Daejeon_nodes_200315.csv') # node list
df_link.to_csv('Daejeon_links_200315.csv') # edge(=link) list
