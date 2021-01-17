# hw4

#### как запускать HalyavaScript code : 
    runST (join $ \your function\ <*> toVar \argument of function\)

#### как конвертировать HalyavaScript code to JS :
    putStr (showCode \your function\)

#### как запускать Comonad-19:
    putStr (showSimulation (simulationNDays \count of days\ startPosition) sizeOfGame)
#### так же вы можете поменять эти парматры чтобы посмотреть другие варианты интерпритаций игры
| Переменная       | За  что отвечает                   |
| ---------------- |:----------------------------------:|
| imDays           | сколько дней будет иммунитет       |
| diDays           | сколько дней человек будет болеть  |
| inDays           | сколько дней человек будет заражен |
| infectionChance  | вероятность заражения              |
| sizeOfGame       | размер игрового поля               |

[![MIT license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com//fp-homework/blob/master/hw4/LICENSE)
=======
### запустить bench tests : stack bench

[![MIT license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com//fp-homework/blob/master/hw4/LICENSE)
>>>>>>> e7d886feca677063d259669543ac4b9cc16c7853
