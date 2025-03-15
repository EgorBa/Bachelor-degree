import React, {useState} from 'react';
import Queries from "../CQRS/Queries.ts";
import Commands from "../CQRS/Commands.ts";

const ManagerRights = () => {
    const [userId, setUserId] = useState('')
    const [infoText, setInfoText] = useState('')

    const handleClickSubscription = () => {
        Queries.checkUser(userId).then(e => {
            if (e) {
                setInfoText("This user has subscription")
            } else {
                Commands.createSub(userId).then(_ => setInfoText('Your subscription has been created'))
            }
        })
    }

    const handleClickExtend = () => {
        Queries.checkUser(userId).then(e => {
            if (!e) {
                setInfoText("This user hasn't subscription")
            } else {
                Commands.extendSub(userId).then(_ => setInfoText('Your subscription has been extended'))
            }
        })
    }

    return (
        <div>
            <input
                type='text'
                placeholder="UserId"
                value={userId}
                onChange={e => setUserId(e.target.value)}/>
            <button
                onClick={handleClickExtend}
            >
                Extend for 1 month
            </button>
            <button
                onClick={handleClickSubscription}
            >
                Create subscription
            </button>
            <div>
                {infoText}
            </div>
        </div>
    );
};

export default ManagerRights;