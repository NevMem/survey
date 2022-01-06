import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import PageWrapper from "../../app/page/PageWrapper";
import CardError from "../../app/card/CardError";
import Card from '../../app/card/Card';
import { ManagedUsersResponse, User } from "../../data/exported";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import Text from '../../components/text/Text';
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import { Fragment } from "react";


const UsersTable = (props: {users: User[]}) => {
    return (
        <Fragment>
            {props.users.map(user => {
                return (
                    <SpaceBetweenRow key={user.id}>
                        <Text>{user.login}</Text>
                        <Text>{user.name}</Text>
                        <Text>{user.surname}</Text>
                        <Text>{user.email}</Text>
                    </SpaceBetweenRow>
                );
            })}
        </Fragment>
    );
};

const AdminPageImpl = () => {
    const request = useAsyncRequest<ManagedUsersResponse>((controller: AbortController) => {
        return backendApi.managedUsers(controller);
    });

    if (request instanceof RequestError) {
        return (
            <CardError>
                {request.message}
            </CardError>
        );
    }

    if (request instanceof RequestSuccess) {
        return (
            <Card>
                <UsersTable users={request.result.users} />
            </Card>
        );
    }

    return (
        <SpaceAroundRow>
            <Loader large />
        </SpaceAroundRow>
    );
};

const AdminPage = () => {
    return (
        <PageWrapper>
            <AdminPageImpl />
        </PageWrapper>
    );
};

export default AdminPage;
