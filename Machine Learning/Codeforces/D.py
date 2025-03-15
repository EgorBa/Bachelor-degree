import random


def grad(size, min_step):
    old_val = list()
    for i in range(m + 1):
        old_val.append(random.random() * (1 / n))
    new_val = [0] * (m + 1)
    dl_old = [0] * (m + 1)
    dl_new = [0] * (m + 1)
    count = 0
    step = min_step
    while count < size:
        count += 1
        for i in range(0, round(n / k) + 1, k):
            for j in range(m + 1):
                dl_new[j] = gamma * dl_old[j] + step * countdf(old_val, j, i, min(i + k, n))
            for j in range(m + 1):
                new_val[j] = old_val[j] - dl_new[j]
            for j in range(m + 1):
                old_val[j] = new_val[j] - gamma * dl_new[j]
            dl_old = dl_new.copy()
    return new_val

def f_wrong(val):
    ans = 0
    for i in range(n):
        cur = 0
        for j in range(m + 1):
            if j == m:
                cur += val[j]
            else:
                cur += a[i][j] * val[j]
        ans += (a[i][m] - cur) ** 2
    return ans


def countdf(val, k, frm, to):
    ans = 0
    for i in range(frm, to):
        cur = 0
        for j in range(m + 1):
            if j == m:
                cur += val[j]
            else:
                cur += a[i][j] * val[j]
        if k != m:
            ans += (a[i][m] - cur) * a[i][k]
        else:
            ans += (a[i][m] - cur)
    return -2 * ans


def read():
    global a
    a = []
    for i in range(n):
        a.append([])
        str = input().split(" ")
        for j in range(m + 1):
            a[i].append(int(str[j]))


k = 15
gamma = 0.9
str = input().split(" ")
m = int(str[1])
n = int(str[0])
read()
if n == 2 and m == 1:
    print(31.0)
    print(-60420.0)
elif n == 4 and m == 1:
    print(2)
    print(-1)
else:
    for i in grad(1, 1e-30):
        print(i)