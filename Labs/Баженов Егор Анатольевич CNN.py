from tensorflow import math
from tensorflow import keras
from numpy import argmax
import matplotlib.pyplot as plt

mnist = keras.datasets.mnist
fashion_mnist = keras.datasets.fashion_mnist

(mnist_train_images, mnist_train_marks), (mnist_test_images, mnist_test_marks) = mnist.load_data()
(fashion_mnist_train_images, fashion_mnist_train_marks), (
    fashion_mnist_test_images, fashion_mnist_test_marks) = fashion_mnist.load_data()

mnist_train_images = mnist_train_images / 255.0
mnist_test_images = mnist_test_images / 255.0

fashion_mnist_train_images = fashion_mnist_train_images / 255.0
fashion_mnist_test_images = fashion_mnist_test_images / 255.0

mnist_train_images = mnist_train_images.reshape(60000, 28, 28, 1)
mnist_test_images = mnist_test_images.reshape(10000, 28, 28, 1)

fashion_mnist_train_images = fashion_mnist_train_images.reshape(60000, 28, 28, 1)
fashion_mnist_test_images = fashion_mnist_test_images.reshape(10000, 28, 28, 1)

models = [keras.Sequential([
    keras.layers.Flatten(input_shape=(28, 28)),
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
]), keras.Sequential([
    keras.layers.Conv2D(32, kernel_size=5, activation='relu', input_shape=(28, 28, 1)),
    keras.layers.Flatten(input_shape=(28, 28)),
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
]), keras.Sequential([
    keras.layers.MaxPool2D(padding="same"),
    keras.layers.Flatten(input_shape=(28, 28)),
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
]), keras.Sequential([
    keras.layers.Conv2D(32, kernel_size=5, activation='relu', input_shape=(28, 28, 1)),
    keras.layers.MaxPool2D(padding="same"),
    keras.layers.Flatten(input_shape=(28, 28)),
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
]), keras.Sequential([
    keras.layers.Conv2D(64, kernel_size=5, activation='relu', input_shape=(28, 28, 1)),
    keras.layers.MaxPool2D(padding="same"),
    keras.layers.Conv2D(32, kernel_size=5, activation='relu', input_shape=(28, 28, 1)),
    keras.layers.MaxPool2D(padding="same"),
    keras.layers.Flatten(input_shape=(28, 28)),
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(10, activation='softmax')
])]

best_test_acc = 0
best_number = 0
number = 0

for model in models:
    model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    model.fit(fashion_mnist_train_images, fashion_mnist_train_marks, epochs=10)
    test_loss, test_acc = model.evaluate(fashion_mnist_test_images, fashion_mnist_test_marks, verbose=1)
    if test_acc > best_test_acc:
        best_test_acc = test_acc
        best_number = number
    number += 1

print("best test accuracy = " + str(best_test_acc))
predict = models[best_number].predict(fashion_mnist_test_images)
p1 = []
for i in range(len(predict)):
    p1.append(argmax(predict[i]))

print(math.confusion_matrix(fashion_mnist_test_marks, p1))
classes = {}
for i in range(len(fashion_mnist_test_marks)):
    if not classes.__contains__(fashion_mnist_test_marks[i]):
        classes[fashion_mnist_test_marks[i]] = []
    classes[fashion_mnist_test_marks[i]].append(i)
# print(classes)

i = 0
for k in range(10):
    for j in range(10):
        i += 1
        m = 0
        num = 0
        for index in classes[k]:
            if predict[index][j] > m:
                m = predict[index][j]
                num = index
        print(num, end=" ")
        print(m, end="        ")
        plt.subplot(10, 10, i)
        plt.xticks([])
        plt.yticks([])
        plt.grid(False)
        plt.imshow(fashion_mnist_train_images[num - 1], cmap=plt.cm.binary)
    print()
plt.show()
