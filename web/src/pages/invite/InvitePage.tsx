import { ChangeEvent, Fragment, useState } from "react";
import PageWrapper from "../../app/page/PageWrapper";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import Text from '../../components/text/Text';
import { AcceptInviteRequest, CreateInviteRequest, CreateInviteResponse, GetInvitesResponse, Invite, InviteStatus } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import GeneralButton from "../../components/button/GeneralButton";
import SpacedColumn from "../../app/layout/SpacedColumn";
import { useModalState, } from "../../components/modal/Modal";
import Badge from "../../components/badge/Badge";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import OutlinedCard from "../../app/card/OutlinedCard";
import CreateInviteModal from "./CreateInviteModal";
import Row from "../../app/layout/Row";
import CardSuccess from "../../app/card/CardSuccess";

const AcceptInviteProcessView = (props: { request: AcceptInviteRequest }) => {
    const request = useAsyncRequest(controller => backendApi.accept(controller, props.request));

    if (request instanceof RequestError) {
        return (
            <CardError>{request.message}</CardError>
        );
    } else if (request instanceof RequestSuccess) {
        return (
            <CardSuccess>Статус: {request.result.status}</CardSuccess>
        );
    }

    return (
        <SpaceAroundRow>
            <Loader />
        </SpaceAroundRow>
    );
};

const IncomingInviteView = (props: { invite: Invite }) => {
    const { invite } = props;

    const [request, setRequest] = useState<AcceptInviteRequest | undefined>();

    const accept = () => {
        setRequest({ id: invite.id });
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text large>{invite.fromUser.name} {invite.fromUser.surname} приглашает вас в проект {invite.project.name}</Text>
                <SpaceAroundRow>
                    <GeneralButton onClick={accept} disabled={invite.status !== InviteStatus.Waiting}>Принять</GeneralButton>
                </SpaceAroundRow>
                { request && <AcceptInviteProcessView request={request} /> }
            </SpacedColumn>
        </OutlinedCard>
    );
};

const InviteView = (props: {invite: Invite, incoming?: boolean}) => {
    const { invite, incoming } = props;

    if (incoming == true) {
        return (
            <IncomingInviteView invite={invite} />
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
                <Text large>Вы пригласили {invite.toUser.name} {invite.toUser.surname} в проект {invite.project.name}</Text>
                <Row>
                    <InviteBadgeView status={invite.status} />
                </Row>
            </SpacedColumn>
        </OutlinedCard>
    );
};

const InvitesTable = (props: { invites: Invite[], incoming?: boolean }) => {
    if (props.invites.length === 0) {
        return (
            <OutlinedCard>
                <SpaceAroundRow>
                    <Text>Нет инвайтов</Text>
                </SpaceAroundRow>
            </OutlinedCard>
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
                <InvitesTable invites={props.invites} />
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
            <InvitesTable invites={props.invites} incoming />
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
