import React, {useState} from 'react';
import Queries from "../CQRS/Queries.ts";
import Commands from "../CQRS/Commands.ts";

const ManagerEnter = ({setIsAuth}) => {
    const [wrongLogin, setWrongLogin] = useState(false)
    const [login, setLogin] = useState('')
    const [pass, setPass] = useState('')

    const cleanInputs = () => {
        setPass('')
        setLogin('')
    }

    const handleClickLogIn = () => {
        Queries.checkManager(login, pass).then(e => {
            setIsAuth(e)
            setWrongLogin(!e)
        })
        cleanInputs()
    }

    const handleClickRegistration = () => {
        Commands.registerManager(login, pass).then(_ => {
            setIsAuth(true)
            setWrongLogin(false)
        })
        cleanInputs()
    }

    return (
        <div>
            <input
                type='text'
                placeholder="Login"
                value={login}
                onChange={e => setLogin(e.target.value)}
            />
            <input
                type='text'
                placeholder="Password"
                value={pass}
                onChange={e => setPass(e.target.value)}
            />
            <button
                onClick={handleClickLogIn}
            >
                Log in
            </button>
            <button
                onClick={handleClickRegistration}
            >
                Register
            </button>
            {wrongLogin && <div>Wrong login or password</div>}
        </div>
    );
};

export default ManagerEnter;