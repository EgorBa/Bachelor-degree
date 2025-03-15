import React, {useState} from 'react';
import ManagerEnter from "../components/ManagerEnter";
import ManagerRights from "../components/ManagerRights";

const Manager = () => {
    const [isAuth, setIsAuth] = useState(false)

    return (
        <div>
            {isAuth
                ? <ManagerRights/>
                : <ManagerEnter setIsAuth={setIsAuth}/>
            }
        </div>
    );
};

export default Manager;