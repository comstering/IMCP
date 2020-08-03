from haversine import haversine

start = (36.8502926, 127.1552497)
goal = (36.8489883, 127.1550046)
result = haversine(start, goal)
print(result)