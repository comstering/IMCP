from haversine import haversine

start = 36.6879651, 126.8368192
goal = 36.833315, 127.1405715
result = haversine(start, goal)
print(result)