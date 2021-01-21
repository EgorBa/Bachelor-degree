import pandas as pv
import random
import math
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors


def count_cl(val, tree):
    i = 0
    while not tree[i][0]:
        if val[tree[i][1]] < tree[i][2]:
            i = tree[i][3]
        else:
            i = tree[i][4]
    return tree[i][1]


def count_p(a, count_class):
    p = {1: 0, -1: 0}
    for i in a:
        p[i[-1]] += 1
    l = len(a)
    p[1] = p[1] / l
    p[-1] = p[-1] / l
    return p


def count_entrop(a, count_class):
    p = {1: 0, -1: 0}
    for i in a:
        p[i[-1]] += 1
    ans = 0
    l = len(a)
    for i in p.values():
        if i == 0:
            continue
        ans += (i / l) ** 2
    return 1 - ans


def train(a, m, count_class, max_h, p, ans):
    global index
    global metrix
    if max_h == 0:
        if p[-1] > p[1]:
            ans.append((True, -1))
        else:
            ans.append((True, 1))
        return index
    e = count_entrop(a, count_class)
    if e == 0:
        if p[-1] > p[1]:
            ans.append((True, -1))
        else:
            ans.append((True, 1))
        return index
    borders = {}
    for k in range(m):
        border = 0
        for i in range(len(a)):
            border += a[i][k]
        border /= len(a)
        first = []
        second = []
        for i in range(len(a)):
            t = a[i][k]
            if t >= border:
                second.append(a[i])
            else:
                first.append(a[i])
        if len(first) == 0 or len(second) == 0:
            continue
        f = count_entrop(first, count_class)
        s = count_entrop(second, count_class)
        borders[e - f * 0.5 - s * 0.5] = (border, first, second, k)
    if len(borders) == 0:
        if p[-1] > p[1]:
            ans.append((True, -1))
        else:
            ans.append((True, 1))
        return index
    key = max(borders.keys())
    border = borders[key][0]
    first = borders[key][1]
    second = borders[key][2]
    k = borders[key][3]
    pos = index
    max_h -= 1
    ans.append((False, k, border, 0, 0))
    index += 1
    i1 = index
    train(first, m, count_class, max_h, count_p(first, count_class), ans)
    index += 1
    i2 = index
    train(second, m, count_class, max_h, count_p(second, count_class), ans)
    ans[pos] = (False, k, border, i1, i2)
    return index


def draw(file, count):
    x1 = list()
    y1 = list()
    cl = list()
    for i in range(len(a)):
        x1.append(a[i][0])
        y1.append(a[i][1])
        if a[i][2] == 1:
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
            ans = 0
            for j in range(len(alphas)):
                hg = count_cl([t2, i], trees[numbers[j]])
                ans += alphas[j] * hg
            if ans > 0:
                ans = 1
            else:
                ans = -1
            if ans == 1:
                cl2.append(1)
            else:
                cl2.append(0)
        t2 += step
    fig, ax = plt.subplots()
    ax.set_title(file + ", количество интераций: " + str(count))
    ax.scatter(x2, y2, c=cl2, cmap=mcolors.ListedColormap(["darksalmon", "paleturquoise"]))
    ax.scatter(x1, y1, c=cl, cmap=mcolors.ListedColormap(["r", "b"]))
    plt.show()


def build_forest(a, h, filename):
    global m
    global count_class
    global index
    global trees
    trees = []
    max_h = h
    count_trees = 100
    for k in range(count_trees):
        random.shuffle(a)
        index = 0
        tree = []
        a1 = random.sample(a, int(len(a) * (1 / 3)))
        while len(a1) < len(a):
            a2 = random.sample(a1, 1)
            a1.append(a2[0].copy())
        train(a1, m, count_class, max_h, count_p(a1, count_class), tree)
        trees.append(tree)
    weight = []
    for i in range(len(a)):
        weight.append(1 / len(a))
    global alphas
    global numbers
    alphas = []
    numbers = []
    x = list()
    y = list()
    go = [1, 2, 3, 5, 8, 13, 21, 34, 55]
    for t in range(56):
        min_wrong = 100000000
        number_alg = -1
        for j in range(len(trees)):
            wrong = 0
            for i in range(len(a)):
                cl = count_cl(a[i], trees[j])
                if cl != a[i][-1]:
                    wrong += weight[i]
            if wrong < min_wrong:
                min_wrong = wrong
                number_alg = j
        alphas.append(0.5 * math.log((1 - min_wrong) / min_wrong))
        numbers.append(number_alg)
        sum = 0
        for i in range(len(a)):
            sum += weight[i] * math.exp(-alphas[-1] * a[i][-1] * count_cl(a[i], trees[numbers[-1]]))
        for i in range(len(a)):
            weight[i] = (weight[i] * math.exp(-alphas[-1] * a[i][-1] * count_cl(a[i], trees[numbers[-1]]))) / sum
        x.append(t)
        wrong = 0
        for str in a:
            ans = 0
            for j in range(len(alphas)):
                ans += alphas[j] * count_cl(str, trees[numbers[j]])
            if ans > 0:
                ans = 1
            else:
                ans = -1
            if ans != str[-1]:
                wrong += 1
        y.append(1 - wrong / len(a))
        if t in go:
            draw(filename, t)
        # print(alphas)
        # print(numbers)
        # print(weight)
    fig, ax = plt.subplots()
    ax.plot(x, y, c='g')
    ax.set_title(filename)
    ax.set_xlabel('количество итераций бустинга')
    ax.set_ylabel('точность')
    plt.show()


def read(filename):
    dataset = pv.read_csv(filename)
    global a
    a = []
    for i in dataset.values:
        str = []
        for k in i:
            if k == 'N':
                str.append(-1)
            elif k == 'P':
                str.append(1)
            else:
                str.append(k)
        a.append(str)


files = ['chips.csv', 'geyser.csv']
a = []
count_class = 2
m = 2
k = 'chips.csv'
read(k)
build_forest(a, 4, k)
k = 'geyser.csv'
read(k)
build_forest(a, 8, k)
