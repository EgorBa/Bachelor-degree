let managers = new Map()
let train = new Map()
let subs = new Map()

// @ts-ignore
class CommandsTest implements CommandsInterface {

    static registerManager = (login, pass) => {
        managers.set(login, pass)
    }

    static createSub = (userId) => {
        subs.set(userId, 1)
    }

    static extendSub = (userId) => {
        subs.set(userId, subs.get(userId) + 1)
    }

    static beginTrain = (userId, date) => {
        train.set(`${userId} ${date}`, 0)
    }

    static endTrain = (userId, date) => {
        train.set(`${userId} ${date}`, date + 1)
    }

}

// @ts-ignore
class QueriesTest implements QueriesInterface {

    static checkManager = (login, pass) => {
        return managers.has(login) && managers.get(login) === pass
    }

    static checkUser = (userId) => {
        return subs.has(userId)
    }

    static checkUserSub = (userId) => {
        return subs.has(userId) && subs.get(userId) > 1
    }

    static getAllInfoTrain = () => {
        return Array.from(train, ([name, value]) => ({name, value}))
    }

}

beforeEach(() => {
    managers = new Map()
    subs = new Map()
    train = new Map()
})

test('check managers after registration', () => {
    CommandsTest.registerManager(1, 2)
    expect(QueriesTest.checkManager(1, 2)).toBe(true)
});

test('check managers without registration', () => {
    expect(QueriesTest.checkManager(1, 2)).toBe(false)
});

test('check creating sub', () => {
    CommandsTest.createSub(1)
    expect(QueriesTest.checkUser(1)).toBe(true)
});

test('check extending sub', () => {
    CommandsTest.createSub(1)
    CommandsTest.extendSub(1)
    expect(QueriesTest.checkUserSub(1)).toBe(true)
});

test('check count trains', () => {
    CommandsTest.beginTrain(1, 0)
    CommandsTest.endTrain(1,0)
    CommandsTest.beginTrain(1, 2)
    CommandsTest.endTrain(1,2)
    expect(QueriesTest.getAllInfoTrain().length).toBe(2)
});

test('check average trains', () => {
    CommandsTest.beginTrain(1, 0)
    CommandsTest.endTrain(1,0)
    CommandsTest.beginTrain(1, 2)
    CommandsTest.endTrain(1,2)
    let count = 0
    let time = 0
    QueriesTest.getAllInfoTrain().forEach(e => {
        count += 1
        time += e.value - e.name.split(" ")[1]
    })
    expect(time / count).toBe(1)
});