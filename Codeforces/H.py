m = int(input())
f = []
for i in range(pow(2, m)):
    f.append(int(input()))
func = []
check = 0
prev = -2
for i in range(pow(2, m)):
    if f[i] == 1:
        check += 1
        p = i
        ar = []
        for k in range(m):
            ar.append(p % 2)
            p = p // 2
        ar.reverse()
        if i == prev + 1 and i % 2 == 1:
            str = func.pop()
            func.append(str[:(m - 1)])
        else:
            func.append(ar)
        prev = i
if check == 0:
    print(1)
    print(1)
    for i in range(m):
        print(0.0, end=" ")
    print(-0.5)
    exit(0)
print(2)
print(len(func), end=" ")
print(1)
first = []
k = 0
for i in func:
    first.append([])
    c = 0
    for j in i:
        if j == 0:
            first[k].append(-1.0)
            c += 1
        else:
            first[k].append(1.0)
    if len(i) == m - 1:
        first[k].append(0)
        first[k].append(c - (m - 1.5))
    else:
        first[k].append(c - (m - 0.5))
    k += 1
second = []
for i in range(len(func)):
    second.append(1.0)
second.append(-0.5)
for i in first:
    for j in i:
        print(j, end=" ")
    print()
for i in second:
    print(i, end=" ")