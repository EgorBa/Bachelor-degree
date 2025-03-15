import pandas as pv
import math
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors


def DBSCAN(ds):
    all = [range(len(ds))]
    all.append([])
    for _ in range(len(ds)):
        all[1].append(False)
    alone = set()
    all_classes = []
    for i in range(len(ds)):
        # print(all)
        # print(alone)
        if all[1][i]:
            continue
        all[1][i] = True
        neighbors = []
        for k in range(len(ds)):
            if dist(i, k, ds) <= eps:
                neighbors.append(k)
        if len(neighbors) - 1 < minPts:
            alone.add(i)
            continue
        ans = [i]
        while len(neighbors) > 0:
            cur_n = neighbors.pop()
            for j in range(len(ds)):
                if all[1][j] and not alone.__contains__(j):
                    continue
                if dist(j, cur_n, ds) <= eps:
                    if alone.__contains__(j):
                        ans.append(j)
                        alone.remove(j)
                        continue
                    count = 0
                    for k in range(len(ds)):
                        if dist(j, k, ds) <= eps:
                            count += 1
                    if count - 1 < minPts:
                        ans.append(j)
                        all[1][j] = True
                    else:
                        ans.append(j)
                        all[1][j] = True
                        neighbors.append(j)
        all_classes.append(ans)
    return all_classes


def dist(i, j, ds):
    first = ds[i]
    second = ds[j]
    ans = 0
    for k in range(len(first)):
        ans += (first[k] - second[k]) ** 2
    return math.sqrt(ans)


def prepare(ds, dataset):
    class_number = 1
    ans = []
    index = 0
    alone = set()
    for i in range(len(dataset)):
        alone.add(i)
    for m in ds:
        for i in m:
            ans.append([])
            ans[index].append(class_number)
            alone.remove(i)
            for j in dataset[i][1:]:
                ans[index].append(j)
            index += 1
        class_number += 1
    for i in alone:
        ans.append([])
        ans[index].append(class_number)
        for j in dataset[i][1:]:
            ans[index].append(j)
        index += 1
    return ans


def PCA(ds):
    mul = np.matrix(ds, dtype=float).T * np.matrix(ds, dtype=float)
    vectors = np.linalg.eig(mul)
    map = {}
    index = 0
    for i in vectors[0]:
        map[i] = vectors[1][index]
        index += 1
    u = np.array([map[sorted(map.keys())[-1]].tolist()[0], map[sorted(map.keys())[-2]].tolist()[0]])
    g = np.matrix(ds, dtype=float) * u.T
    return g


def dataset_minmax(ds):
    minmax = list()
    for i in range(len(ds[0])):
        if i == len(ds[0]):
            continue
        value_min = ds[:, i].min()
        value_max = ds[:, i].max()
        minmax.append([value_min, value_max])
    return minmax


def normalize(ds, minmax):
    for row in ds:
        for i in range(len(row)):
            if i == len(row):
                continue
            row[i] = (row[i] - minmax[i][0]) / (minmax[i][1] - minmax[i][0])


def count_rand(ds, dataset):
    tp = 0.0
    tn = 0.0
    fp = 0.0
    fn = 0.0
    for i in range(len(ds)):
        for j in range(len(dataset)):
            if ds[i][0] == ds[j][0] and dataset[i][0] == dataset[j][0]:
                tp += 1
            if ds[i][0] != ds[j][0] and dataset[i][0] == dataset[j][0]:
                tn += 1
            if ds[i][0] == ds[j][0] and dataset[i][0] != dataset[j][0]:
                fp += 1
            if ds[i][0] != ds[j][0] and dataset[i][0] != dataset[j][0]:
                fn += 1
    return (tp + fn) / (tp + tn + fp + fn)


def count_a(x, cl, ds):
    if len(cl) == 1:
        return 0
    ans = 0.0
    for i in cl:
        if i == x:
            continue
        ans += dist(i, x, ds)
    ans /= (len(cl) - 1)
    return ans


def count_b(x, cl, ds, dataset):
    mx = math.inf
    for c_l in ds:
        ans = 0.0
        if c_l == cl:
            continue
        for i in c_l:
            ans += dist(i, x, dataset)
        ans /= len(c_l)
        if ans < mx:
            mx = ans
    return mx


def count_silhouette(ds, dataset):
    ans = 0.0
    l = 0.0
    for c_l in ds:
        for x in c_l:
            l += 1
            b = count_b(x, c_l, ds, dataset)
            a = count_a(x, c_l, dataset)
            ans += (b - a) / max(a, b)
    if l == 0:
        return 0
    ans /= l
    if math.isnan(ans):
        return 0
    return ans


def draw(ds, title):
    g = PCA(ds[:, 1:])
    cl = []
    x = []
    y = []
    index = 0
    for i in g[:, 0]:
        cl.append(ds.tolist()[index][0])
        index += 1
        x.append(i)
    for i in g[:, 1]:
        y.append(i)
    fig, ax = plt.subplots()
    ax.set_title(title)
    ax.scatter(x, y, c=cl, cmap=mcolors.ListedColormap(['blue', 'green', 'green', 'black', 'y']))
    plt.show()


def draw_graph():
    x = list()
    y = list()
    y1 = list()
    global eps
    fig, ax = plt.subplots()
    ax.set_xlabel('значение eps')
    for e in range(50):
        eps = 0.3 + e / 100
        x.append(eps)
        kek = DBSCAN(dataset.values[:, 1:])
        y.append(count_rand(prepare(kek, dataset.values), dataset.values))
        y1.append(count_silhouette(kek, dataset.values[:, 1:]))
    plt.plot(x, y, label="зависимость коэф. Rand от eps")
    plt.plot(x, y1, label="зависимость коэф. Silhouette от eps")
    plt.legend()
    plt.show()


def find_best():
    global eps
    global minPts
    best_clast = (0, 0)
    for e in range(20):
        eps = 0.45 + e / 100
        for m in range(10):
            minPts = m + 3
            clusters = DBSCAN(dataset.values[:, 1:])
            if len(clusters) == 3 and count_rand(prepare(clusters, dataset.values), dataset.values) > max_rand:
                print("silhouette = ", count_silhouette(clusters, dataset.values[:, 1:]))
                max_rand = count_rand(prepare(clusters, dataset.values), dataset.values)
                print("rand = ", max_rand)
                print("eps = ", eps)
                print("minPts = ", minPts)
                best_clast = (eps, minPts)
    eps = best_clast[0]
    minPts = best_clast[1]


eps = 0.46
minPts = 12
filename = 'wine.csv'
dataset = pv.read_csv(filename, dtype=float)
min_max = dataset_minmax(dataset.values[:, 1:])
normalize(dataset.values[:, 1:], min_max)
best_clast = []
max_rand = 0
# find_best()
clusters = DBSCAN(dataset.values[:, 1:])
best_clast = prepare(clusters, dataset.values)
draw(np.matrix(best_clast), "Результат кластеризации")
draw(dataset.values, "Исходные данные")
draw_graph()
