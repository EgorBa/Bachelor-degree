import math
import os
import matplotlib.pyplot as plt


def counti(j):
    c = 0
    for k in range(i):
        if int(a[k][0]) == j:
            c += 1
    return c


l = [10, 1]
files = os.listdir('messages')
alpha = 10
goods = {}
for f in files:
    files1 = files.copy()
    files1.remove(f)
    i = 0
    a = []
    for f1 in files1:
        for doc in os.listdir('messages\\' + f1):
            a.append([])
            if 'sp' in doc:
                a[i].append(0)
            else:
                a[i].append(1)
            str = list()
            file = open('messages\\' + f1 + '\\' + doc)
            str.append(file.readline().split(" "))
            str.append(file.readline().split(" "))
            str.append(file.readline().split(" "))
            file.close()
            for j in range(1, len(str[0]) - 1):
                a[i].append(int(str[0][j]))
            for j in range(len(str[2])):
                a[i].append(int(str[2][j]))
            i += 1
    s = set()
    keyVal = {}
    # print(a)
    for k in range(i):
        for j in range(1, len(a[k])):
            s.add(a[k][j])
    for t in range(2):
        keyVal[t] = {}
        for j in s:
            keyVal[t][j] = 0
    for k in range(i):
        ss = set()
        for j in range(1, len(a[k])):
            ss.add(a[k][j])
        for j in ss:
            keyVal[int(a[k][0])][j] += 1
    prob = list()
    c = [0] * 2
    for j in range(2):
        prob.append({})
        c[j] = counti(j)
        for k in s:
            prob[j][k] = (keyVal[j][k] + alpha) / (c[j] + 2 * alpha)
    cnt = 0
    tp = 0
    for doc in os.listdir('messages\\' + f):
        b = []
        if 'sp' in doc:
            cl = 0
        else:
            cl = 1
        str = list()
        file = open('messages\\' + f + '\\' + doc)
        str.append(file.readline().split(" "))
        str.append(file.readline().split(" "))
        str.append(file.readline().split(" "))
        file.close()
        for j in range(1, len(str[0]) - 1):
            b.append(int(str[0][j]))
        for j in range(len(str[2])):
            b.append(int(str[2][j]))
        c1 = [0] * 2
        s1 = set()
        for j in b:
            s1.add(j)
        for t in range(2):
            for key in s:
                if key in s1:
                    c1[t] += math.log(float(prob[t][key]))
                else:
                    c1[t] += math.log(1 - float(prob[t][key]))
            c1[t] += math.log(c[t] / i)
            c1[t] += math.log(l[t])
        ans = list()
        for t in range(2):
            C1 = 0
            for y in range(2):
                C1 += math.exp(c1[y] - c1[t])
            ans.append(1 / C1)
        if (ans[0] > ans[1] and cl == 1) or (ans[1] > ans[0] and cl == 0):
            tp += 1
        cnt += 1
    goods[tp / cnt] = (f, prob, i, c, s)
print("find goods")
key = min(goods.keys())
prob = goods[key][1]
f = goods[key][0]
i = goods[key][2]
c = goods[key][3]
s = goods[key][4]
x1 = list()
y1 = list()
mas = list()
cnt = 0
y_go = 0
x_go = 0
for doc in os.listdir('messages\\' + f):
    b = []
    if 'sp' in doc:
        cl = 0
        x_go += 1
    else:
        cl = 1
        y_go += 1
    str = list()
    file = open('messages\\' + f + '\\' + doc)
    str.append(file.readline().split(" "))
    str.append(file.readline().split(" "))
    str.append(file.readline().split(" "))
    file.close()
    for j in range(1, len(str[0]) - 1):
        b.append(int(str[0][j]))
    for j in range(len(str[2])):
        b.append(int(str[2][j]))
    c1 = [0] * 2
    s1 = set()
    for j in b:
        s1.add(j)
    for t in range(2):
        for key in s:
            if key in s1:
                c1[t] += math.log(float(prob[t][key]))
            else:
                c1[t] += math.log(1 - float(prob[t][key]))
        c1[t] += math.log(c[t] / i)
        c1[t] += math.log(l[t])
    ans = list()
    for t in range(2):
        C1 = 0
        for y in range(2):
            C1 += math.exp(c1[y] - c1[t])
        ans.append(1 / C1)
    mas.append((ans[1], cl))
    cnt += 1
y_go = 1 / y_go
x_go = 1 / x_go
y_v = 0
x_v = 0
mas.sort()
mas.reverse()
for t in range(cnt):
    if mas[t][1] == 1:
        y_v += y_go
        x1.append(x_v)
        y1.append(y_v)
    else:
        x_v += x_go
        x1.append(x_v)
        y1.append(y_v)
plt.plot(x1, y1, label="ROC-кривая")
plt.legend()
plt.show()
x1 = list()
y1 = list()
for t1 in range(l[0], int(l[1]) * 10, int(l[1])):
    l[1] = t1
    tn = 0
    tp = 0
    for doc in os.listdir('messages\\' + f):
        b = []
        if 'sp' in doc:
            cl = 0
        else:
            cl = 1
        str = list()
        file = open('messages\\' + f + '\\' + doc)
        str.append(file.readline().split(" "))
        str.append(file.readline().split(" "))
        str.append(file.readline().split(" "))
        file.close()
        for j in range(1, len(str[0]) - 1):
            b.append(int(str[0][j]))
        for j in range(len(str[2])):
            b.append(int(str[2][j]))
        c1 = [0] * 2
        s1 = set()
        for j in b:
            s1.add(j)
        for t in range(2):
            for key in s:
                if key in s1:
                    c1[t] += math.log(float(prob[t][key]))
                else:
                    c1[t] += math.log(1 - float(prob[t][key]))
            c1[t] += math.log(c[t] / i)
            c1[t] += math.log(l[t])
        ans = list()
        for t in range(2):
            C1 = 0
            for y in range(2):
                C1 += math.exp(c1[y] - c1[t])
            ans.append(1 / C1)
        if ans[1] > ans[0] and cl == 1:
            tp += 1
        if ans[1] > ans[0] and cl == 0:
            tn += 1
    x1.append(t1)
    y1.append(tp / (tn + tp))
plt.plot(x1, y1, label="точность от значания альфа")
plt.legend()
plt.show()
