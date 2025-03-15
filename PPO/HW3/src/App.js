import * as Realm from "realm-web";
import {useEffect, useState} from "react";
import {Route, Routes} from "react-router";
import Manager from "./pages/Manager";
import User from "./pages/User";
import Statistics from "./pages/Statistics";

const REALM_APP_ID = "application-0-gitfk";
const app = new Realm.App({ id: REALM_APP_ID });

function App() {
    const [isLoading, setIsLoading] = useState(true)

    const loginAnonymous = async () => {
        return await app.logIn(Realm.Credentials.anonymous())
    };

    useEffect(() => {
        if (app.currentUser === null) {
            loginAnonymous().then(_ => setIsLoading(false))
        }
    }, [])

    return (
        <Routes>
            <Route path="user" element={<User/>}/>
            <Route path="stat" element={<Statistics/>}/>
            <Route path="man" element={<Manager/>}/>
        </Routes>
    );
}

export default App;
