import { useState } from "react";
import Card from "../../app/card/Card";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpacedColumn from "../../app/layout/SpacedColumn";
import PageWrapper from "../../app/page/PageWrapper";
import GeneralButton from "../../components/button/GeneralButton";
import Input from "../../components/input/Input";
import Text from "../../components/text/Text";
import authService, { LoginError, LoginSuccess } from "../../service/authorization/AuthorizationService";

const LoginBlock = (props: {switchToRegister: () => void}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | undefined>(undefined);

    const performLogin = () => {
        authService.login(login, password)
            .then(result => {
                if (result instanceof LoginError) {
                    setError(result.message);
                } else if (result instanceof LoginSuccess) {
                    console.log("Hehehe!!!");
                }
            });
    };

    return (
        <PageWrapper>
            <Card>
                <SpacedCenteredColumn rowGap={32}>
                    <SpacedCenteredColumn rowGap={16}>
                        <Text large>Пожалуйста войдите</Text>
                        
                        <SpacedColumn rowGap={4}>
                            <Text>Логин</Text>
                            <Input onChange={ev => setLogin(ev.target.value)} value={login} />

                            <Text>Пароль</Text>
                            <Input onChange={ev => setPassword(ev.target.value)} value={password} type='password' />
                        </SpacedColumn>
                    </SpacedCenteredColumn>
                    <SpacedCenteredColumn rowGap={16}>
                        <SpaceAroundRow><GeneralButton onClick={performLogin}>Войти</GeneralButton></SpaceAroundRow>
                        <SpaceAroundRow><GeneralButton secondary onClick={props.switchToRegister}>Зарегистрироваться</GeneralButton></SpaceAroundRow>
                    </SpacedCenteredColumn>
                </SpacedCenteredColumn>
            </Card>
        </PageWrapper>
    );
};

const RegisterBlock = (props: {switchToLogin: () => void}) => {
    return (
        <PageWrapper>
            <Card>
                <SpacedCenteredColumn rowGap={16}>
                    <SpaceAroundRow><GeneralButton>Зарегистрироваться</GeneralButton></SpaceAroundRow>
                    <SpaceAroundRow><GeneralButton secondary onClick={props.switchToLogin}>Войти</GeneralButton></SpaceAroundRow>
                </SpacedCenteredColumn>
            </Card>
        </PageWrapper>
    );
};

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
