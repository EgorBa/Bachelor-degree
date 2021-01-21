import random
import pandas as pv
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
import math


def train(max_counter):
    counter = 0
    flag = True
    is_changed = False
    while (counter < max_counter) and (is_changed or flag):
        is_changed = False
        if flag:
            for i in range(n):
                is_changed = is_changed or process(i)
            counter += 1
        else:
            queue = []
            for i in range(n):
                if 0 < alphas[i] < c:
                    queue.append(i)
            for i in queue:
                is_changed = is_changed or process(i)
            counter += 1
        if flag:
            flag = False
        else:
            flag = not is_changed


def func_e(k):
    ans = 0
    for i in range(n):
        ans += float(alphas[i] * y[i] * func_k(i, k))
    return ans + b - y[k]


def find_second(k, ei):
    j = 0
    max = -1
    e[k] = ei
    queue = []
    for j in range(n):
        if e[j] != 0:
            queue.append(j)
    if (len(queue) > 1):
        for i in queue:
            if i == k:
                continue
            error = func_e(i)
            if abs(error - ei) > max:
                j = i
                max = abs(error - ei)
        return j
    else:
        j = k
        while j == k:
            j = int(random.random() * n)
        return j


def func_k(i, j):
    ans = 0
    if type_f == 'Linear':
        for k in range(m):
            ans += dataset[j][k] * dataset[i][k]
    elif type_f == 'Polynomial':
        for k in range(m):
            ans += dataset[j][k] * dataset[i][k]
        ans += c
        ans = ans ** d
    else:
        for k in range(m):
            ans += (dataset[j][k] - dataset[i][k]) ** 2
        ans = math.exp(-ans / 2 * sigma)
    return ans


def process(i):
    ei = func_e(i)
    if ((y[i] * ei < -eps) and (alphas[i] < c)) or ((y[i] * ei > eps) and (alphas[i] < 0)):
        j = find_second(i, ei)
        ej = func_e(j)
        alpha_i = alphas[i]
        alpha_j = alphas[j]
        if y[j] == y[i]:
            g = alphas[j] + alphas[i]
            if (g > c):
                L = g - c
                H = c
            else:
                L = 0
                H = g
        else:
            g = alphas[i] - alphas[j]
            if (g > 0):
                L = 0
                H = c - g
            else:
                L = -g
                H = c
        if L == H:
            return False
        nu = 2 * func_k(i, j) - func_k(i, i) - func_k(j, j)
        if nu >= 0:
            return False
        alphas[j] -= y[j] * (ei - ej) * (1 / nu)
        if alphas[j] > H:
            alphas[j] = H
        elif alphas[j] < L:
            alphas[j] = L
        e[j] = func_e(j)
        if abs(alphas[j] - alpha_j) < eps:
            return False
        alphas[i] += y[j] * y[i] * (alpha_j - alphas[j])
        e[i] = func_e(i)
        global b
        b1 = b - ei - y[i] * (alphas[i] - alpha_i) * func_k(i, i) - y[j] * (
                alphas[j] - alpha_j) * func_k(i, j)
        b2 = b - ej - y[i] * (alphas[i] - alpha_i) * func_k(i, j) - y[j] * (
                alphas[j] - alpha_j) * func_k(j, j)
        if 0 < alphas[i] < c:
            b = b1
        elif 0 < alphas[j] < c:
            b = b2
        else:
            b = (b1 + b2) / 2
        return True
    else:
        return False


def func_k1(i, val):
    ans = 0
    if type_f == 'Linear':
        for k in range(m):
            ans += val[k] * dataset[i][k]
    elif type_f == 'Polynomial':
        for k in range(m):
            ans += val[k] * dataset[i][k]
        ans += c
        ans = ans ** d
    else:
        for k in range(m):
            ans += (val[k] - dataset[i][k]) ** 2
        ans = math.exp(-ans / 2 * sigma)
    return ans


def func_h(k, flag):
    ans = 0
    if flag:
        for i in range(n):
            ans += float(alphas[i] * y[i] * func_k(i, k))
    else:
        for i in range(n):
            ans += float(alphas[i] * y[i] * func_k1(i, k))
    ans += b
    if ans > 0:
        return 1
    else:
        return -1


def read(filename):
    global dataset
    global y
    global n
    global m
    data = pv.read_csv(filename)
    n = int(data.values.size / 3)
    m = 2
    dataset = [[0 for n in range(m)] for nn in range(n)]
    y = [0] * n
    for i in range(n):
        for j in range(m):
            dataset[i][j] = data.values[i][j]
        if data.values[i][m] == "P":
            y[i] = 1
        else:
            y[i] = -1


def draw(file):
    x1 = list()
    y1 = list()
    cl = list()
    for i in range(n):
        x1.append(dataset[i][0])
        y1.append(dataset[i][1])
        if y[i] == 1:
            cl.append(1)
        else:
            cl.append(0)
    if file == 'chips.csv':
        step = 0.01
    else:
        step = 0.1
    x2 = list()
    y2 = list()
    cl2 = list()
    t1 = max(x1)
    t2 = min(x1)
    v1 = max(y1)
    v2 = min(y1)
    while t2 <= t1:
        i = v2
        while i <= v1:
            x2.append(t2)
            y2.append(i)
            i += step
            if func_h([t2, i], False) == 1:
                cl2.append(1)
            else:
                cl2.append(0)
        t2 += step
    plt.title("type_f=" + type_f + " for dataset " + filename)
    plt.scatter(x2, y2, c=cl2, cmap=mcolors.ListedColormap(["darksalmon", "paleturquoise"]))
    plt.scatter(x1, y1, c=cl, cmap=mcolors.ListedColormap(["r", "b"]))
    plt.show()


sigma = 10
d = 2.1
eps = 0.001
files = ['chips.csv', 'geyser.cvs']
function = ['Linear', 'Polynomial', 'Gaussian']
for i0 in files:
    filename = i0
    read(i0)
    variants = {}
    for i01 in function:
        type_f = i01
        for i1 in range(10, 100, 10):
            c = i1
            for i2 in range(1000, 9000, 1000):
                # print("work")
                max_count = i2
                e = [0] * n
                b = 0
                alphas = [0] * n
                train(max_count)
                wrong = 0
                for i in range(n):
                    if y[i] != func_h(i, True):
                        wrong += 1
                    variants[wrong / n] = [alphas, b, i1, i2]
        key = min(variants.keys())
        alphas = variants[key][0]
        b = variants[key][1]
        print(variants[key])
        draw(i0)
