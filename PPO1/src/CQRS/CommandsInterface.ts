interface CommandsInterface {
    registerManager: (login: string, pass: string) => any,
    createSub: (userId: string) => any,
    extendSub: (userId: string) => any,
    beginTrain: (userId: string, date: Date) => any
    endTrain: (userId: string, date: Date) => any
}