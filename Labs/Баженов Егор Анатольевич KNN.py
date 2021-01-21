import pandas as pv
import math
import numpy as np
import matplotlib.pyplot as plt


def dataset_minmax(dataset):
    minmax = list()
    for i in range(len(dataset[0])):
        if i == len(dataset[0]) - 1:
            continue
        value_min = dataset[:, i].min()
        value_max = dataset[:, i].max()
        minmax.append([value_min, value_max])
    return minmax


def normalize(dataset, minmax):
    for row in dataset:
        for i in range(len(row)):
            if i == len(row) - 1:
                continue
            row[i] = (row[i] - minmax[i][0]) / (minmax[i][1] - minmax[i][0])


def distance(row1, row2):
    distance = 0.0
    for i in range(len(row1) - 1):
        if type_distance == 'euclidean':
            distance += ((row1[i] - row2[i]) ** 2)
        if type_distance == 'manhattan':
            distance += abs(row1[i] - row2[i])
        if type_distance == 'chebyshev':
            distance += max(row1[i] - row2[i], distance)
    if type_distance == 'euclidean':
        distance = math.sqrt(distance)
    return distance


def get_neighbors(train, row_number):
    distances = list()
    test_row = train[row_number]
    for train_number in range(len(train)):
        if train_number == row_number:
            continue
        dist = distance(test_row, train[train_number])
        distances.append((train[train_number], dist))
    if type_w == 'variable':
        distances.sort(key=lambda tup: tup[1])
    return distances


def funcK(u):
    if type_func == 'uniform':
        if abs(u) < 1:
            return 0
        return 0.5
    if type_func == 'triangular':
        if abs(u) < 1:
            return 0
        return 1 - abs(u)
    if type_func == 'epanechnikov':
        if abs(u) < 1:
            return 0
        return 0.75 * (1 - u * u)
    if type_func == 'quartic':
        if abs(u) < 1:
            return 0
        return (15 / 16) * ((1 - u * u) ** 2)
    if type_func == 'triweight':
        if abs(u) < 1:
            return 0
        return (35 / 32) * ((1 - u * u) ** 3)
    if type_func == 'tricube':
        if abs(u) < 1:
            return 0
        return (70 / 81) * ((1 - u * u * abs(u)) ** 3)
    if type_func == 'gaussian':
        return math.exp(-0.5 * u * u) / math.sqrt(2 * math.pi)
    if type_func == 'cosine':
        if abs(u) < 1:
            return 0
        return math.pi * math.cos(math.pi * u / 2) / 4
    if type_func == 'logistic':
        return 1 / (math.exp(u) + 2 + math.exp(-u))
    if type_func == 'sigmoid':
        return 2 / (math.pi * (math.exp(u) + math.exp(-u)))
    return 0


def predict(train, row_number, num_neighbors):
    test_row = train[row_number]
    neighbor = get_neighbors(train, row_number)
    if type_w == 'fixed':
        k = num_neighbors
    else:
        k = distance(test_row, neighbor[num_neighbors][0])
    first = 0.0
    second = 0.0
    if k == 0:
        sum = 0
        count = 0
        for n in neighbor:
            if distance(test_row, n[0]) == 0:
                sum += n[0][-1]
                count += 1
        if count == 0:
            for n in neighbor:
                sum += n[0][-1]
                count += 1
        if math.isnan(sum / count):
            return 0
        else:
            return sum / count
    else:
        for n in neighbor:
            v = distance(test_row, n[0]) / k
            try:
                f = funcK(v)
            except OverflowError:
                f = 0
            first += n[0][-1] * f
            second += f
        if second != 0:
            prediction = first / second
        else:
            sum = 0.0
            for i in range(len(train)):
                if i == row_number:
                    continue
                sum += train[i][- 1]
            prediction = sum / (len(train) - 1)
        if math.isnan(prediction):
            return 0
        else:
            return prediction


def count_f(matrix):
    k = len(matrix)
    f_func = 0.0
    all = 0.0
    for i in range(k):
        for j in range(k):
            all += matrix[i][j]
    for i in range(k):
        sum_row = 0.0
        for j in range(k):
            sum_row += matrix[i][j]
        sum_col = 0.0
        for j in range(k):
            sum_col += matrix[j][i]
        if sum_row != 0:
            p = matrix[i][i] / sum_row
        else:
            p = 0.0
        if sum_col != 0:
            r = matrix[i][i] / sum_col
        else:
            r = 0.0
        if matrix[i][i] != 0:
            f_func += (2 * p * r * (sum_row / all) / (p + r))
    return f_func


def build_one_hot(dataset):
    m = pv.DataFrame()
    for i in dataset.keys().values:
        if i == 'Class':
            m = pv.concat([m, dataset[i]], axis=1)
            continue
        m = pv.concat([m, pv.get_dummies(dataset[i], prefix='V')], axis=1)
    return m


def draw(var, dataset, cl):
    best = min(var.keys())
    global type_distances
    type_distances = var[best][0]
    global type_func
    type_func = var[best][1]
    global type_w
    type_w = var[best][2]
    global point
    point = var[best][3]
    print(var[best])
    x = list()
    y = list()
    colors = list()
    for h in range(1, 10):
        matrix = np.zeros((count_classes, count_classes))
        for row_number in range(len(dataset)):
            a = round(predict(dataset.values, row_number, h))
            matrix[int(dataset.values[row_number][- 1]) - 1][a] += 1
        if h == point:
            colors.append('r')
        else:
            colors.append(cl)
        x.append(h)
        y.append(count_f(matrix))
    fig, ax = plt.subplots()
    ax.plot(x, y, c=cl)
    ax.set_xlabel('размер окна')
    ax.set_ylabel('f-мера')
    plt.show()


filename = 'heart.csv'
dataset = pv.read_csv(filename)
one_hot_dataset = build_one_hot(dataset.copy())
min_max = dataset_minmax(dataset.values)
normalize(dataset.values, min_max)
types_distances = ['euclidean', 'chebyshev', 'manhattan']
types_func = ['uniform', 'triangular', 'epanechnikov', 'quartic', 'triweight', 'tricube', 'gaussian', 'cosine',
              'logistic', 'sigmoid']
types_w = ['fixed', 'variable']
count_classes = 5
variants = {}
variants_one_hot = {}
for i in types_distances:
    type_distance = i
    for j in types_func:
        type_func = j
        for k in types_w:
            type_w = k
            for h in range(1, 10):
                wrong = 0
                wrong_one_hot = 0
                for row_number in range(len(dataset)):
                    a = round(predict(dataset.values, row_number, h))
                    a_one_hot = round(predict(one_hot_dataset.values, row_number, h))
                    if a != dataset.values[row_number][- 1]:
                        wrong += 1
                    if a_one_hot != one_hot_dataset.values[row_number][- 1]:
                        wrong_one_hot += 1
                variants[wrong / len(dataset.values)] = (type_distance, type_func, type_w, h)
                variants_one_hot[wrong_one_hot / len(dataset.values)] = (type_distance, type_func, type_w, h)

draw(variants, dataset, 'g')
draw(variants_one_hot, one_hot_dataset, 'b')
