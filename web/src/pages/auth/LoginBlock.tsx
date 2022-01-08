import PageWrapper from "../../app/page/PageWrapper";
import CardError from "../../app/card/CardError";
import Card from "../../app/card/Card";
import Text from "../../components/text/Text";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import GeneralButton from "../../components/button/GeneralButton";
import { useState } from "react";
import authService, { LoginError, LoginSuccess } from "../../service/authorization/AuthorizationService";
import SpacedColumn from "../../app/layout/SpacedColumn";
import Input from "../../components/input/Input";
import CardSuccess from "../../app/card/CardSuccess";


const LoginBlock = (props: {switchToRegister: () => void}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | undefined>(undefined);
    const [success, setSuccess] = useState(false);

    const performLogin = () => {
        authService.login(login, password)
            .then(result => {
                if (result instanceof LoginError) {
                    setError(result.message);
                } else if (result instanceof LoginSuccess) {
                    setError(undefined);
                    setSuccess(true);
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

                    {error && <CardError><Text>{error}</Text></CardError>}

                    {success && <CardSuccess><Text>Залогинились</Text></CardSuccess>}

                    <SpacedCenteredColumn rowGap={16}>
                        <SpaceAroundRow><GeneralButton onClick={performLogin}>Войти</GeneralButton></SpaceAroundRow>
                        <SpaceAroundRow><GeneralButton secondary onClick={props.switchToRegister}>Зарегистрироваться</GeneralButton></SpaceAroundRow>
                    </SpacedCenteredColumn>
                </SpacedCenteredColumn>
            </Card>
        </PageWrapper>
    );
};

export default LoginBlock;