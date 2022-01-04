import { Fragment, useState } from "react";
import PageWrapper from "../../app/page/PageWrapper";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import Text from '../../components/text/Text';
import { GetInvitesResponse, Invite } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import Card from "../../app/card/Card";

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

function useAsyncRequest<T>(request: Promise<T>): RequestState {
    const [state, setState] = useState(new RequestProcessing());

    request.then(data => { setState(new RequestSuccess<T>(data)); })
        .catch(error => { setState(new RequestError(error + "")); });

    return state;
};

const InvitesTable = (props: { invites: Invite[] }) => {
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

const InvitePage = () => {
    const result = useAsyncRequest<GetInvitesResponse>(backendApi.invites());

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
        return (
            <PageWrapper>
                <Card>
                    <InvitesTable invites={result.result.invites} />
                </Card>
            </PageWrapper>
        );
    }

    return (
        <PageWrapper>
            <Loader large />
        </PageWrapper>
    );
};

export default InvitePage;
