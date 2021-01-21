str = input().split(" ")
k1 = int(str[0])
k2 = int(str[1])
table = {}
n = int(input())
x = []
set_x = set()
set_y = set()
y = []
for i in range(n):
    str = input().split(" ")
    x.append(int(str[0]))
    set_x.add(x[i])
    y.append(int(str[1]))
    set_y.add(y[i])
    if not table.__contains__((x[i], y[i])):
        table[(x[i], y[i])] = 0
    table[(x[i], y[i])] += 1

map1 = {}
for i in range(n):
    if not map1.__contains__(x[i]):
        map1[x[i]] = list()
    map1[x[i]].append(y[i])

s1 = 0
for lst in map1.keys():
    if len(map1[lst]) > 0:
        map1[lst] = len(map1[lst])
        s1 += map1[lst]

map2 = {}
for i in range(n):
    if not map2.__contains__(y[i]):
        map2[y[i]] = list()
    map2[y[i]].append(x[i])

s2 = 0
for lst in map2.keys():
    if len(map2[lst]) > 0:
        map2[lst] = len(map2[lst])
        s2 += map2[lst]

ans = 0.0
ans += s2 * s1 / n
for p in table.keys():
    o = map1[p[0]] * map2[p[1]] / n
    ans -= o
    ans += ((table[p] - o) ** 2) / o

print(ans)