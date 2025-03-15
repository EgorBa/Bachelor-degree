import random


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


def find_second(k):
    j = k
    while j == k:
        j = int(random.random() * n)
    return j


def func_k(i, j):
    return dataset[i][j]


def process(i):
    ei = func_e(i)
    if ((y[i] * ei < -eps) and (alphas[i] < c)) or ((y[i] * ei > eps) and (alphas[i] < 0)):
        j = find_second(i)
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
        if nu > 0:
            return False
        alphas[j] -= y[j] * (ei - ej) * (1 / nu)
        if alphas[j] > H:
            alphas[j] = H
        elif alphas[j] < L:
            alphas[j] = L
        if abs(alphas[j] - alpha_j) < r:
            return False
        alphas[i] += y[j] * y[i] * (alpha_j - alphas[j])
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


eps = 1e-15
r = 1e-15
b = 0
n = int(input())
dataset = [[0 for n in range(n)] for nn in range(n)]
y = [0] * n
alphas = [0] * n
for i in range(n):
    str = input().split(" ")
    for j in range(n):
        dataset[i][j] = int(str[j])
    y[i] = int(str[n])
c = int(input())
train(1000)
for i in alphas:
    print(i)
print(b)