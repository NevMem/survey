import { ChangeEvent, Fragment, useState } from "react";
import PageWrapper from "../../app/page/PageWrapper";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import Text from '../../components/text/Text';
import { CreateInviteRequest, CreateInviteResponse, GetInvitesResponse, Invite, InviteStatus } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import Card from "../../app/card/Card";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import GeneralButton from "../../components/button/GeneralButton";
import SpacedColumn from "../../app/layout/SpacedColumn";
import { ModalActions, ModalBody, ModalHeader, ModalView, useModalState, ModalState } from "../../components/modal/Modal";
import { Option, Select } from "../../components/select/Selector";
import Badge from "../../components/badge/Badge";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import OutlinedCard from "../../app/card/OutlinedCard";

const InviteView = (props: {invite: Invite, incoming?: boolean}) => {
    const { invite, incoming } = props;
    if (incoming == true) {
        return (
            <OutlinedCard>
                <SpacedColumn rowGap={16}>
                    <Text>{invite.fromUser.name} {invite.fromUser.surname} приглашает вас в проект {invite.project.name}</Text>
                    <GeneralButton>Принять</GeneralButton>
                </SpacedColumn>
            </OutlinedCard>
        );
    }

    const InviteBadgeView = (props: { status: InviteStatus }) => {
        const { status } = props;
        if (status === InviteStatus.Accepted) {
            return <Badge success>Принят</Badge>
        } else if (status === InviteStatus.Expired) {
            return <Badge error>Просрочен</Badge>
        } else {
            return <Badge info>Ждет подтверждения</Badge>
        }
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text>Вы пригласили {invite.fromUser.name} {invite.fromUser.surname} в проект {invite.project.name}</Text>
                <InviteBadgeView status={invite.status} />
            </SpacedColumn>
        </OutlinedCard>
    );
    // return (
    //     <SpaceBetweenRow>
    //         <Text>{props.invite.inviteId}</Text>
    //         {props.invite.acceptedBy ? <Badge success>{props.invite.acceptedBy.login}</Badge> : <Badge error>not accepted</Badge>}
    //         {props.invite.isExpired ? <Badge error>просрочился</Badge> : <Badge success>активен</Badge>}
    //     </SpaceBetweenRow>
    // );
};

const InvitesTable = (props: { invites: Invite[], incoming?: boolean }) => {
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
                    return <InviteView invite={invite} key={index} incoming={props.incoming} />;
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
        return (
            <Card>
                Создали инвайт: {actual.invite.inviteId}
            </Card>
        );
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
        // const request: CreateInviteRequest = {
        //     expirationTimeSeconds: option.seconds,
        // };
        // setRequest(request);
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

const OutgoingActualPage = (props: { invites: Invite[] }) => {
    const modalState = useModalState();

    const openCreateInviteView = () => {
        modalState.open();
    };

    return (
        <Fragment>
            <CreateInviteModal state={modalState} />
            <SpacedColumn rowGap={16}>
                <SpaceBetweenRow>
                    <Text header>Инвайты</Text>
                    <GeneralButton onClick={openCreateInviteView}>Создать инвайт</GeneralButton>
                </SpaceBetweenRow>
                <Card>
                    <InvitesTable invites={props.invites} />
                </Card>
            </SpacedColumn>
        </Fragment>
    );
};

const IncomingActualPage = (props: { invites: Invite[] }) => {
    return (
        <SpacedColumn rowGap={16}>
            <SpaceBetweenRow>
                <Text header>Входящие инвайты</Text>
            </SpaceBetweenRow>
            <Card>
                <InvitesTable invites={props.invites} incoming />
            </Card>
        </SpacedColumn>
    );
};

const IncomingInvitesBlock = () => {
    const result = useAsyncRequest<GetInvitesResponse>((controller: AbortController) => backendApi.incomingInvites(controller));

    if (result instanceof RequestError) {
        return (
            <CardError>
                <Text large>{result.message}</Text>
            </CardError>
        );
    }

    if (result instanceof RequestSuccess) {
        return <IncomingActualPage invites={result.result.invites} />;
    }

    return (
        <Loader large />
    );
};

const OutgoingInvitesBlock = () => {
    const result = useAsyncRequest<GetInvitesResponse>((controller: AbortController) => backendApi.outgoingInvites(controller));

    if (result instanceof RequestError) {
        return (
            <CardError>
                <Text large>{result.message}</Text>
            </CardError>
        );
    }

    if (result instanceof RequestSuccess) {
        return <OutgoingActualPage invites={result.result.invites} />;
    }

    return (
        <Loader large />
    );
};

const InvitePage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={32}>
                <IncomingInvitesBlock />
                <OutgoingInvitesBlock />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default InvitePage;
