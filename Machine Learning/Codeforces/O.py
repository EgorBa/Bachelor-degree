def count(l):
    map2 = {}
    for i in range(len(l)):
        if not map2.__contains__(l[i]):
            map2[l[i]] = 0
        map2[l[i]] += 1
    for i in map2.keys():
        map2[i] /= len(l)
    return map2


k = int(input())
n = int(input())
x = []
y = []
for i in range(n):
    str = input().split(" ")
    x.append(int(str[0]))
    y.append(int(str[1]))

map1 = {}
for i in range(n):
    if not map1.__contains__(x[i]):
        map1[x[i]] = list()
    map1[x[i]].append(y[i])


for lst in map1.keys():
    if len(map1[lst]) > 0:
        map1[lst] = count(map1[lst])

map3 = count(x)
a = 0.0
for q in map1.keys():
    mpp = map1[q]
    m = 0.0
    ans = 0.0
    for i in mpp.keys():
        m += mpp[i] * i
    for i in mpp.keys():
        ans += ((i - m) ** 2) * mpp[i]
    a += ans*map3[q]

print(a)