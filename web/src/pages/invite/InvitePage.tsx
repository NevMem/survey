import { Fragment, useEffect, useState } from "react";
import PageWrapper from "../../app/page/PageWrapper";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import Text from '../../components/text/Text';
import { GetInvitesResponse, Invite } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import Card from "../../app/card/Card";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import GeneralButton from "../../components/button/GeneralButton";
import SpacedColumn from "../../app/layout/SpacedColumn";

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
            {props.invites.map((invite, index) => {
                return (
                    <SpaceAroundRow key={index}>
                        <Text>{invite.inviteId}</Text>
                        <Text>{invite.isExpired}</Text>
                    </SpaceAroundRow>
                )
            })}
        </Fragment>
    );
};

const ActualPage = (props: { invites: Invite[] }) => {
    const createInvite = () => {
        
    };

    return (
        <PageWrapper>
            <SpacedColumn rowGap={16}>
                <SpaceBetweenRow>
                    <Text header>Инвайты</Text>
                    <GeneralButton onClick={createInvite}>Создать инвайт</GeneralButton>
                </SpaceBetweenRow>
                <Card>
                    <InvitesTable invites={props.invites} />
                </Card>
            </SpacedColumn>
        </PageWrapper>
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
