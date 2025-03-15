import numpy
from keras.models import Sequential
from keras.models import load_model
from keras.layers import Dense
from keras.layers import LSTM
from keras.utils import np_utils
from keras.preprocessing.sequence import pad_sequences


def get_word(string):
    x = pad_sequences([string], maxlen=m, dtype='float32')
    x = numpy.reshape(x, (1, m, 1))
    x = x / float(len(alphabet))
    prediction = model.predict(x, verbose=0)
    index = numpy.argmax(prediction)
    result = int_to_char[index]
    if result == '$':
        return ""
    else:
        ans = string
        ans.append(index)
        return result + get_word(ans)


# fix random seed for reproducibility
numpy.random.seed(7)
# define the raw dataset
alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя-$"
# create mapping of characters to integers (0-34) and the reverse
char_to_int = dict((c, i) for i, c in enumerate(alphabet))
int_to_char = dict((i, c) for i, c in enumerate(alphabet))
# prepare the dataset of input to output pairs encoded as integers
f = open('word_rus.txt', encoding='utf-8')
# num_inputs = 34010
num_inputs = 1000
begin_arr = []
max_len = 4
m = 10
dataX = []
dataY = []
for i in range(num_inputs):
    string = f.readline()
    if len(string) <= max_len or len(string) > m:
        continue
    begin_arr.append([char_to_int[char] for char in string[:max_len]])
    for j in range(len(string) - max_len):
        start = string[:(j + max_len)]
        if j == len(string) - max_len - 1:
            end = '$'
        else:
            end = string[j + max_len]
        dataX.append([char_to_int[char] for char in start])
        dataY.append(char_to_int[end])
# convert list of lists to array and pad sequences if needed
X = pad_sequences(dataX, maxlen=m, dtype='float32')
# reshape X to be [samples, time steps, features]
X = numpy.reshape(X, (X.shape[0], m, 1))
# normalize
X = X / float(len(alphabet))
# one hot encode the output variable
y = np_utils.to_categorical(dataY)
# create and fit the model
batch_size = 1
model = Sequential()
model.add(LSTM(32, input_shape=(X.shape[1], 1)))
model.add(Dense(y.shape[1], activation='softmax'))
model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
# model.fit(X, y, epochs=1000, batch_size=batch_size, verbose=1)
# model.save('model.h5')
model = load_model('model.h5')
# summarize performance of the model
scores = model.evaluate(X, y, verbose=1)
print("Model Accuracy: %.2f%%" % (scores[1] * 100))
# demonstrate some model predictions
for i in range(20):
    pattern_index = numpy.random.randint(len(begin_arr))
    pattern = begin_arr[pattern_index]
    seq_in = [int_to_char[value] for value in pattern]
    print(seq_in, "->", end="")
    ans = get_word(pattern)
    print(ans)
