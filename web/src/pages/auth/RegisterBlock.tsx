import PageWrapper from "../../app/page/PageWrapper";
import Card from "../../app/card/Card";
import Text from "../../components/text/Text";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import GeneralButton from "../../components/button/GeneralButton";
import { ChangeEvent, useState } from "react";
import Input from "../../components/input/Input";
import SpacedColumn from "../../app/layout/SpacedColumn";
import backendApi from '../../api/backendApiServiceSingleton';
import { instanceOfRegisterSuccessful, RegisterRequest, RegisterResponse } from "../../data/exported";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import Loader from "../../components/loader/Loader";
import CardError from "../../app/card/CardError";
import CardSuccess from "../../app/card/CardSuccess";

const useTextInput = () => {
    const [value, setValue] = useState('');

    const changeHandler = (event: ChangeEvent<HTMLInputElement>) => { setValue(event.target.value) }

    return {
        value: value,
        changeHandler: changeHandler,
    };
};

const RegisterProcessingBlock = (props: {request: RegisterRequest}) => {
    const result = useAsyncRequest<RegisterResponse>((controller) => {
        return backendApi.register(
            props.request.name,
            props.request.surname,
            props.request.login,
            props.request.password,
            props.request.email,
            props.request.inviteId,
            controller,
        );
    });

    if (result instanceof RequestSuccess) {
        if (instanceOfRegisterSuccessful(result.result)) {
            return (
                <CardSuccess>
                    <Text>Удачно зарегистрировались</Text>
                </CardSuccess>
            )
        }
    }

    if (result instanceof RequestError) {
        return (
            <CardError>
                <Text>{result.message}</Text>
            </CardError>
        );
    }

    return <SpaceAroundRow><Loader large /></SpaceAroundRow>;
};

const RegisterBlock = (props: {switchToLogin: () => void}) => {

    const login = useTextInput();
    const password = useTextInput();
    const name = useTextInput();
    const surname = useTextInput();
    const email = useTextInput();
    const inviteId = useTextInput();

    const fields = [
        {label: 'Логин', state: login},
        {label: 'Пароль',state: password},
        {label: 'Имя', state: name},
        {label: 'Фамилия', state: surname},
        {label: 'Почта', state: email},
        {label: 'Инвайт', state: inviteId},
    ];

    const [request, setRequest] = useState<RegisterRequest | undefined>();

    const register = () => {
        const req: RegisterRequest = {
            name: name.value,
            surname: surname.value,
            login: login.value,
            password: password.value,
            email: email.value,
            inviteId: inviteId.value,
        };
        setRequest(req);
    };

    return (
        <PageWrapper>
            <Card>
                <SpacedCenteredColumn rowGap={24}>
                    <SpacedCenteredColumn rowGap={8}>
                        {fields.map(elem => {
                            return (
                                <SpacedColumn key={elem.label} rowGap={4}>
                                    <Text>{elem.label}</Text>
                                    <Input value={elem.state.value} onChange={elem.state.changeHandler} />
                                </SpacedColumn>
                            );
                        })}
                    </SpacedCenteredColumn>
                    {request && <RegisterProcessingBlock request={request} />}
                    <SpacedCenteredColumn rowGap={16}>
                        <SpaceAroundRow><GeneralButton onClick={register}>Зарегистрироваться</GeneralButton></SpaceAroundRow>
                        <SpaceAroundRow><GeneralButton secondary onClick={props.switchToLogin}>Войти</GeneralButton></SpaceAroundRow>
                    </SpacedCenteredColumn>
                </SpacedCenteredColumn>
            </Card>
        </PageWrapper>
    );
};

export default RegisterBlock;
