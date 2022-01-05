import { ChangeEvent, Fragment, useEffect, useState } from "react";
import PageWrapper from "../../app/page/PageWrapper";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import Text from '../../components/text/Text';
import { CreateInviteRequest, CreateInviteResponse, GetInvitesResponse, instanceOfCreateInviteError, instanceOfCreateInviteSuccess, Invite } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import Card from "../../app/card/Card";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import GeneralButton from "../../components/button/GeneralButton";
import SpacedColumn from "../../app/layout/SpacedColumn";
import { ModalActions, ModalBody, ModalHeader, ModalView, useModalState, ModalState } from "../../components/modal/Modal";
import { Option, Select } from "../../components/select/Selector";
import Badge from "../../components/badge/Badge";

interface RequestState {};
class RequestProcessing implements RequestState {};
class RequestError implements RequestState {
    message: string;
    constructor(message: string) {
        this.message = message;
    }
};
class RequestSuccess<T> implements RequestState {
    result: T;
    constructor(result: T) {
        this.result = result;
    }
};

function useAsyncRequest<T>(requestBuilder: (abortController: AbortController) => Promise<T>): RequestState {
    const [state, setState] = useState(new RequestProcessing());

    useEffect(() => {
        const abortController = new AbortController();
        requestBuilder(abortController)
            .then(data => { setState(new RequestSuccess<T>(data)); })
            .catch(error => { setState(new RequestError(error + "")); });
        return () => {
            abortController.abort();
        };
    }, []);

    return state;
};

const InviteView = (props: {invite: Invite}) => {
    return (
        <SpaceBetweenRow>
            <Text>{props.invite.inviteId}</Text>
            <Text>{props.invite.isExpired ? <Badge error>просрочился</Badge> : <Badge success>активен</Badge>}</Text>
        </SpaceBetweenRow>
    );
};

const InvitesTable = (props: { invites: Invite[] }) => {
    if (props.invites.length === 0) {
        return (
            <SpaceAroundRow>
                <Text>Нет инвайтов</Text>
            </SpaceAroundRow>
        );
    }

    return (
        <Fragment>
            <SpacedColumn rowGap={16}>
                {props.invites.map((invite, index) => {
                    return <InviteView invite={invite} key={index} />;
                })}
            </SpacedColumn>
        </Fragment>
    );
};

const InviteCreationRequestBlock = (props: { requestBuilder: (abortController: AbortController) => Promise<CreateInviteResponse> }) => {
    const result = useAsyncRequest(props.requestBuilder);

    if (result instanceof RequestError) {
        return <CardError><Text>{result.message}</Text></CardError>;
    }

    if (result instanceof RequestSuccess) {
        const actual = result.result;
        if (instanceOfCreateInviteError(actual)) {
            return <CardError><Text>{actual.message}</Text></CardError>;
        }
        if (instanceOfCreateInviteSuccess(actual)) {
            return (
                <Card>
                    Создали инвайт: {actual.invite.inviteId}
                </Card>
            );
        }
    }

    return <SpaceAroundRow><Loader large /></SpaceAroundRow>;
};

const CreateInviteModal = (props: { state: ModalState }) => {
    const availableExpirationTimes = [
        {
            caption: '1 минута',
            seconds: 60,
        },
        {
            caption: '10 минут',
            seconds: 60 * 10,
        },
        {
            caption: '1 час',
            seconds: 60 * 60,
        },
        {
            caption: '6 часов',
            seconds: 60 * 60 * 6,
        },
        {
            caption: '1 сутки',
            seconds: 60 * 60 * 24,
        },
    ];

    const [option, setOption] = useState(availableExpirationTimes[0]);
    const [request, setRequest] = useState<CreateInviteRequest | undefined>(undefined);

    const createInvite = () => {
        const request: CreateInviteRequest = {
            expirationTimeSeconds: option.seconds,
        };
        setRequest(request);
    };

    const changeSelection = (event: ChangeEvent<HTMLSelectElement>) => {
        setOption(availableExpirationTimes.find(time => time.caption === event.target.value)!!)
    };

    return (
        <ModalView state={props.state}>
            <ModalHeader>
                <Text large>Создать инвайт</Text>
            </ModalHeader>
            <ModalBody>
                <SpacedColumn rowGap={16}>
                    {request && <InviteCreationRequestBlock requestBuilder={(controller: AbortController) => backendApi.createInvite(request, controller)} />}
                    <Select onChange={changeSelection}>
                        {availableExpirationTimes.map(elem => {
                            return <Option key={elem.seconds}>{elem.caption}</Option>;
                        })}
                    </Select>
                </SpacedColumn>
            </ModalBody>
            <ModalActions>
                <GeneralButton onClick={createInvite}>Создать</GeneralButton>
                <GeneralButton secondary onClick={() => props.state.close()}>Отмена</GeneralButton>
            </ModalActions>
        </ModalView>
    );
}

const ActualPage = (props: { invites: Invite[] }) => {
    const modalState = useModalState();

    const openCreateInviteView = () => {
        modalState.open();
    };

    return (
        <Fragment>
            <CreateInviteModal state={modalState} />
            <PageWrapper>
                <SpacedColumn rowGap={16}>
                    <SpaceBetweenRow>
                        <Text header>Инвайты</Text>
                        <GeneralButton onClick={openCreateInviteView}>Создать инвайт</GeneralButton>
                    </SpaceBetweenRow>
                    <Card>
                        <InvitesTable invites={props.invites} />
                    </Card>
                </SpacedColumn>
            </PageWrapper>
        </Fragment>
    );
};

const InvitePage = () => {
    const result = useAsyncRequest<GetInvitesResponse>((controller: AbortController) => backendApi.invites(controller));

    if (result instanceof RequestError) {
        return (
            <PageWrapper>
                <CardError>
                    <Text large>{result.message}</Text>
                </CardError>
            </PageWrapper>
        );
    }

    if (result instanceof RequestSuccess) {
        return <ActualPage invites={result.result.invites} />;
    }

    return (
        <PageWrapper>
            <Loader large />
        </PageWrapper>
    );
};

export default InvitePage;
