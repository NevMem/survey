import PageWrapper from "../../app/page/PageWrapper";
import CardError from "../../app/card/CardError";
import Text from "../../components/text/Text";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import GeneralButton from "../../components/button/GeneralButton";
import { useState } from "react";
import authService, { LoginError, LoginSuccess } from "../../service/authorization/AuthorizationService";
import SpacedColumn from "../../app/layout/SpacedColumn";
import Input from "../../components/input/Input";
import CardSuccess from "../../app/card/CardSuccess";
import OutlinedCard from "../../app/card/OutlinedCard";
import TextButton from "../../components/button/TextButton";


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
            <SpacedColumn rowGap={24}>
                <Text header>Вход</Text>
                <OutlinedCard>
                    <SpacedCenteredColumn rowGap={32}>
                        <SpacedCenteredColumn rowGap={16}>
                            <Text large>Пожалуйста войдите</Text>
                            
                            <SpacedColumn rowGap={4} style={{width: '340px'}}>
                                <Text>Логин</Text>
                                <Input onChange={ev => setLogin(ev.target.value)} value={login} />

                                <Text>Пароль</Text>
                                <Input onChange={ev => setPassword(ev.target.value)} value={password} type='password' />
                            </SpacedColumn>
                        </SpacedCenteredColumn>

                        {error && <CardError><Text>{error}</Text></CardError>}

                        {success && <CardSuccess><Text>Залогинились</Text></CardSuccess>}

                        <SpacedCenteredColumn rowGap={16}>
                            <SpaceAroundRow>
                                <GeneralButton onClick={performLogin}>Войти</GeneralButton>
                            </SpaceAroundRow>
                            <SpaceAroundRow>
                                <TextButton secondary onClick={props.switchToRegister}>Зарегистрироваться</TextButton>
                            </SpaceAroundRow>
                        </SpacedCenteredColumn>
                    </SpacedCenteredColumn>
                </OutlinedCard>
            </SpacedColumn>
        </PageWrapper>
    );
};

export default LoginBlock;
