import numpy as np
import math
import random
import matplotlib.pyplot as plt


def c(i, j):
    ans = 0
    for k in range(n):
        ans += a[k][j] * a[k][i]
    return ans


def MNK():
    matrix = np.ones((n, m + 1))
    val = np.zeros(m + 1)
    for i in range(n):
        for j in range(m):
            matrix[i][j] = a[i][j]
    matrix = np.linalg.pinv(matrix)
    for i in range(m + 1):
        for j in range(n):
            val[i] += matrix[i][j] * a[j][m]
    return val


def countdf(val, k):
    ans = 0
    for i in range(n):
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


def f_wrong_args(val):
    ans = np.zeros(n)
    for i in range(n):
        cur = 0
        for j in range(m + 1):
            if j == m:
                cur += val[j]
            else:
                cur += a[i][j] * val[j]
        ans[i] = cur
    return ans


def grad(size):
    old_val = np.zeros(m + 1)
    new_val = np.zeros(m + 1)
    min_step = 1e-15
    step = min_step
    for i in range(size):
        df = np.zeros(m + 1)
        for j in range(m + 1):
            df[j] = countdf(old_val, j)
        for j in range(m + 1):
            new_val[j] = old_val[j] - step * df[j]
        old_val = new_val
        y2.append(nrmse(new_val))
    return new_val


def f_new(s, t):
    ans = s
    for i in range(m + 1):
        ans[i] += random.uniform(- t, t)
    return ans


def t_new(i, t_max):
    i += 1
    return t_max / i


def nrmse(val):
    f = f_wrong(val)
    m_val = abs(max(f_wrong_args(val)) - min(f_wrong_args(val)))
    return (100 / m_val) * math.sqrt((1 / n) * f)


def anneal(t_max):
    s = np.zeros((m + 1))
    old_val = np.zeros((m + 1))
    t_min = 0.1
    t = t_max
    i = 1
    while t > t_min:
        s = f_new(old_val, t)
        de = f_wrong(s) - f_wrong(old_val)
        if de <= 0:
            old_val = s
        else:
            i = math.exp(-de / t)
            if random.random() <= i:
                old_val = s
        i += 1
        t = t_new(i, t_max)
        y3.append(nrmse(s))
        if i > count_of_iter:
            break
    return s


def draw(cl, lg):
    x = list()
    y1 = list()
    global y2
    y2 = list()
    global y3
    y3 = list()
    mnk = nrmse(MNK())
    print("mnk")
    grad(count_of_iter - 1)
    print("grad")
    anneal(500)
    print("anneal")
    y4 = list()
    for i in range(1, count_of_iter):
        x.append(i)
        y1.append(mnk)
        y4.append(y3[i])
    plt.plot(x, y1, c=cl[0], label=lg[0])
    plt.plot(x, y2, c=cl[1], label=lg[1])
    plt.plot(x, y4, c=cl[2], label=lg[2])


def read():
    global n
    n = int(f.readline())
    global a
    a = np.zeros((n, m + 1))
    for i in range(n):
        str = f.readline().split(" ")
        for j in range(m + 1):
            a[i][j] = int(str[j])


f = open('3.txt', 'r')
count_of_iter = 200
m = int(f.readline())
read()
fig, ax = plt.subplots()
ax.set_xlabel('количество интераций')
ax.set_ylabel('nrmse')
draw(['g', 'b', 'r'], ['train MNK', 'train grad', 'train anneal'])
read()
draw(['c', 'm', 'y'], ["test MNK", "test grad", "test anneal"])
plt.legend()
plt.show()
