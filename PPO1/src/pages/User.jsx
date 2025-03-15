import React, {useState} from 'react';
import Queries from "../CQRS/Queries.ts";
import Commands from "../CQRS/Commands.ts";


const User = () => {
    const[isEnter, setIsEnter] = useState(false)
    const [userId, setUserId] = useState('')
    const [date, setDate] = useState(new Date())
    const [infoText, setInfoText] = useState('')

    const handleClickEnter = () => {
        if (isEnter) {
            Commands.endTrain(userId, date).then(_ => {
                setIsEnter(false)
            })
        } else {
            Queries.checkUserSub(userId).then(e => {
                if (e) {
                    let date1 = new Date()
                    setDate(date1)
                    Commands.beginTrain(userId, date1).then(_ => {
                        setIsEnter(true)
                        setInfoText('')
                    })
                } else {
                    setInfoText('Your subscription isn`t available')
                }
            })
        }
    }

    return (
        <div>
            {!isEnter && <input
                type='text'
                placeholder="UserId"
                value={userId}
                onChange={e => setUserId(e.target.value)}
            />}
            <button
                onClick={handleClickEnter}
            >
                {isEnter ? "Exit from fitness" : "Enter to fitness"}
            </button>
            <div>
                {isEnter &&
                <img
                    src={require('../resources/cat.gif')}
                    alt="cat"/>}
                {isEnter &&
                <img
                    src={require('../resources/train.gif')}
                    alt="cat"/>}
            </div>
            <div>
                {infoText}
            </div>
        </div>
    );
};

export default User;