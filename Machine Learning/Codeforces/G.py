import math


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


def count_entrop(a, count_class):
    p = {}
    for i in range(count_class):
        p[i] = 0
    for i in a:
        p[i[-1] - 1] += 1
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
        ans.append((True, p))
        return index
    e = count_entrop(a, count_class)
    if e == 0:
        ans.append((True, p))
        return index
    borders = {}
    for k in range(m):
        border = 0
        s = set()
        for i in range(len(a)):
            s.add(a[i][k])
            # border += a[i][k]
        srt = sorted(s)
        l = len(srt)
        if l % 2 == 0:
            border = srt[l // 2]
            border /= 1
        else:
            border = srt[(l // 2) - 1]
            border /= 1
        # border = border / len(a)
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
        borders[e - f * (len(first) / len(a)) - s * (len(second) / len(a))] = (border, first, second, k)
    if len(borders) == 0:
        ans.append((True, p))
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


str = input().split(" ")
m = int(str[0])
k = int(str[1])
h = int(str[2])
n = int(input())
a = []
tree = []
index = 0
for i in range(n):
    str = input().split(" ")
    a.append([])
    for j in range(m + 1):
        a[i].append(int(str[j]))
train(a, m, k, min(h, 7), count_p(a, k), tree)
print(len(tree))
for i in range(len(tree)):
    node = tree[i]
    if len(node) == 2:
        print('C', end=" ")
        ans = 0
        cl = 0
        for j in range(len(node[1])):
            if node[1][j] >= ans:
                ans = node[1][j]
                cl = j
        print(cl + 1)
    else:
        print('Q', end=" ")
        print(node[1] + 1, end=" ")
        print(node[2], end=" ")
        print(node[3] + 1, end=" ")
        print(node[4] + 1)