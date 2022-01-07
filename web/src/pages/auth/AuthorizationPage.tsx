import { useState } from "react";
import RegisterBlock from './RegisterBlock';
import LoginBlock from "./LoginBlock";

const AuthorizationPage = () => {
    const [mode, setMode] = useState('login');

    const switchToRegister = () => {
        setMode('register');
    };

    const switchToLogin = () => {
        setMode('login');
    };

    if (mode === 'login') {
        return <LoginBlock switchToRegister={switchToRegister} />
    }
    return <RegisterBlock switchToLogin={switchToLogin} />
};

export default AuthorizationPage;
