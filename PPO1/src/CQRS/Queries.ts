import * as Realm from "realm-web";

const REALM_APP_ID = "application-0-gitfk";
const app = new Realm.App({id: REALM_APP_ID});

export default // @ts-ignore
class Queries implements QueriesInterface {

    static async checkManager(login, pass) {
        const manager = await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("manager")
            .findOne({pass: pass, login: login})
        return manager !== null
    }

    static async checkUser(userId) {
        const manager = await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("fit")
            .findOne({userId: userId})
        return manager !== null
    }

    static async checkUserSub(userId) {
        const manager = await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("fit")
            .findOne({userId: userId})
        return manager !== null && manager.date >= new Date()
    }

    static async getAllInfoTrain() {
        return await app.currentUser.mongoClient("mongodb-atlas")
            .db("test-db")
            .collection("train")
            .find()
    }
}