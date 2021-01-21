import math
import matplotlib.pyplot as plt
import random


def count_class_by_tree(br, tree):
    node = tree[0]
    while not node[0]:
        if node[1] <= br:
            node = tree[node[3]]
        else:
            node = tree[node[2]]

    r = random.random()
    ans = 0
    for i in range(len(node[1])):
        if node[1][i] > node[1][ans]:
            ans = i
    return ans


def count_entrop(a, count_class):
    p = {}
    for i in range(count_class):
        p[i] = 0
    for i in a:
        p[i[-1] - 1] += 1
    ans = 0
    l = len(a)
    for i in p:
        if i == 0:
            continue
        ans -= (i / l) * math.log(i / l)
    return ans


def count_p(a, count_class):
    p = {}
    for i in range(count_class):
        p[i] = 0
    for i in a:
        p[i[-1] - 1] += 1
    l = len(a)
    for i in range(count_class):
        p[i] = p[i] / l
    return p


def metr(val):
    ans = 0
    for i in range(len(val) - 1):
        ans += val[i] ** 2
    return math.sqrt(ans)


def train(a, m, count_class, max_h, p, ans):
    global index
    global metrix
    if max_h == 0:
        ans.append((True, p))
        return index
    e = count_entrop(a, count_class)
    if e == 0:
        ans.append((True, p))
        return index
    borders = {}
    for i in range(len(a)):
        border = metrix[i]
        first = []
        second = []
        for j in range(len(a)):
            m = metrix[j]
            if m > border:
                second.append(a[j])
            else:
                first.append(a[j])
        if len(first) == 0 or len(second) == 0:
            continue
        f = count_entrop(first, count_class)
        s = count_entrop(second, count_class)
        if f >= 0 and s >= 0:
            borders[e - (f + s) / 2] = (border, first, second)
    if len(borders) == 0:
        ans.append((True, p))
        return index
    key = max(borders.keys())
    border = borders[key][0]
    first = borders[key][1]
    second = borders[key][2]
    pos = index
    max_h -= 1
    ans.append((False, border, 0, 0))
    index += 1
    i1 = index
    train(first, m, count_class, max_h, count_p(first, count_class), ans)
    index += 1
    i2 = index
    train(second, m, count_class, max_h, count_p(second, count_class), ans)
    ans[pos] = (False, border, i1, i2)
    return index


def draw(number):
    x = list()
    y = list()
    y1 = list()
    global metrix
    for max_h in range(1, 20):
        x.append(max_h)
        metrix = []
        filename = open('DT_txt\\' + number + "_test.txt")
        str = filename.readline().split(" ")
        m = int(str[0])
        count_class = int(str[1])
        n = int(filename.readline())
        a = []
        tree = []
        for i in range(n):
            a.append([])
            for j in filename.readline().split(" "):
                a[i].append(int(j))
            metrix.append(metr(a[i]))
        filename.close()
        global index
        index = 0
        wrong = 0
        train(a, m, count_class, max_h, count_p(a, count_class), tree)
        for i in range(len(a)):
            cl = count_class_by_tree(metrix[i], tree)
            if cl != a[i][-1]:
                wrong += 1
        y1.append(wrong / n)
        filename = open('DT_txt\\' + number + "_train.txt")
        metrix = []
        str = filename.readline().split(" ")
        m = int(str[0])
        count_class = int(str[1])
        n = int(filename.readline())
        wrong = 0
        a = []
        for i in range(n):
            a.append([])
            for j in filename.readline().split(" "):
                a[i].append(int(j))
            metrix.append(metr(a[i]))
        filename.close()
        for i in range(len(a)):
            cl = count_class_by_tree(metrix[i], tree)
            if cl != a[i][-1]:
                wrong += 1
        y.append(wrong / n)
    plt.title(number + ".txt tree")
    plt.plot(x, y, c='g', label='test')
    plt.plot(x, y1, c='r', label='train')
    plt.legend()
    plt.show()


def build_forest(number):
    x = list()
    y = list()
    y1 = list()
    global metrix
    for max_h in range(1, 20):
        x.append(max_h)
        metrix = []
        filename = open('DT_txt\\' + number + "_test.txt")
        str = filename.readline().split(" ")
        m = int(str[0])
        count_class = int(str[1])
        n = int(filename.readline())
        a = []
        tree1 = []
        tree2 = []
        tree3 = []
        tree4 = []
        for i in range(n):
            a.append([])
            for j in filename.readline().split(" "):
                a[i].append(int(j))
            metrix.append(metr(a[i]))
        filename.close()
        global index
        index = 0
        wrong = 0
        a1 = []
        a2 = []
        a3 = []
        a4 = []
        for i in range(len(a)):
            r = random.random()
            if r < 0.25:
                a1.append(a[i])
            elif r < 0.5:
                a2.append(a[i])
            elif r < 0.75:
                a3.append(a[i])
            elif r <= 1:
                a4.append(a[i])
        train(a1, m, count_class, max_h, count_p(a1, count_class), tree1)
        index = 0
        train(a2, m, count_class, max_h, count_p(a2, count_class), tree2)
        index = 0
        train(a3, m, count_class, max_h, count_p(a3, count_class), tree3)
        index = 0
        train(a4, m, count_class, max_h, count_p(a4, count_class), tree4)
        for i in range(len(a)):
            cl1 = count_class_by_tree(metrix[i], tree1)
            cl2 = count_class_by_tree(metrix[i], tree2)
            cl3 = count_class_by_tree(metrix[i], tree3)
            cl4 = count_class_by_tree(metrix[i], tree4)
            cl = (cl1 + cl2 + cl3 + cl4) // 4
            if cl != a[i][-1]:
                wrong += 1
        y1.append(wrong / n)
        filename = open('DT_txt\\' + number + "_train.txt")
        metrix = []
        str = filename.readline().split(" ")
        m = int(str[0])
        count_class = int(str[1])
        n = int(filename.readline())
        wrong = 0
        a = []
        for i in range(n):
            a.append([])
            for j in filename.readline().split(" "):
                a[i].append(int(j))
            metrix.append(metr(a[i]))
        filename.close()
        for i in range(len(a)):
            cl1 = count_class_by_tree(metrix[i], tree1)
            cl2 = count_class_by_tree(metrix[i], tree2)
            cl3 = count_class_by_tree(metrix[i], tree3)
            cl4 = count_class_by_tree(metrix[i], tree4)
            cl = (cl1 + cl2 + cl3 + cl4) // 4
            if cl != a[i][-1]:
                wrong += 1
        y.append(wrong / n)
        print(wrong / n)
        # print(tree)
    plt.title(number + ".txt forest")
    plt.plot(x, y, c='g', label='test')
    plt.plot(x, y1, c='r', label='train')
    plt.legend()
    plt.show()


a = []
tree = []
metrix = []
index = 0
draw('05')
build_forest('05')
draw('20')
build_forest('20')
draw('21')
build_forest('21')
