import * as Realm from "realm-web";

const REALM_APP_ID = "application-0-gitfk";
const app = new Realm.App({ id: REALM_APP_ID });

export default // @ts-ignore
class Commands implements CommandsInterface {

    static async registerManager(login, pass) {
        await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("manager")
            .insertOne({login: login, pass: pass})
    }

    static async createSub(userId) {
        const date = new Date()
        date.setMonth(date.getMonth() + 1)
        await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("fit")
            .insertOne({userId: userId, date: date})
    }

    static async extendSub(userId) {
        const sub = await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("fit")
            .findOne({userId: userId})
        const newDate = sub.date
        newDate.setMonth(newDate.getMonth() + 1)
        await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("fit")
            .findOneAndUpdate({userId: userId},
                {userId: userId, date: newDate})
    }

    static async beginTrain(userId, date) {
        await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("train")
            .insertOne({userId: `${userId} ${date}`, start: date, end: date})
    }

    static async endTrain(userId, date) {
        const user = await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("train")
            .findOne({userId: `${userId} ${date}`})
        await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("train")
            .findOneAndUpdate({userId: `${userId} ${date}`},
                {userId: `${userId} ${date}`, start: user.start, end: new Date()})
    }

}