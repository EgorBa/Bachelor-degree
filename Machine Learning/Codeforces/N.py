k = int(input())
n = int(input())
x = []
y = []
for i in range(n):
    str = input().split(" ")
    x.append(int(str[0]))
    y.append(int(str[1]))

ans1 = 0
ans2 = 0
map1 = {}
for i in range(n):
    if not map1.__contains__(y[i]):
        map1[y[i]] = list()
    map1[y[i]].append(x[i])

for lst in map1.keys():
    if len(map1[lst]) > 0:
        map1[lst].sort()

for lst in map1.keys():
    size = len(map1[lst]) - 1
    cof = [0] * size
    for i in range(size // 2):
        k1 = (i + 1) * size - ((i + 1) * i)
        cof[i] = k1
        cof[size - 1 - i] = k1
    if size % 2 != 0:
        cof[size // 2] = (size // 2 + 1) * size - ((size // 2 + 1) * (size // 2))
    for i in range(size):
        r = map1[lst][i + 1] - map1[lst][i]
        ans1 += 2 * r * cof[i]
x.sort()
size = n - 1
cof = [0] * size
for i in range(size // 2):
    k1 = (i + 1) * size - ((i + 1) * i)
    cof[i] = k1
    cof[size - 1 - i] = k1
if size % 2 != 0:
    cof[size // 2] = (size // 2 + 1) * size - ((size // 2 + 1) * (size // 2))
for i in range(size):
    r = x[i + 1] - x[i]
    ans2 += 2 * r * cof[i]
print(ans1)
print(ans2 - ans1)