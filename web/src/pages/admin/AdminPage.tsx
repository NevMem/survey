import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import PageWrapper from "../../app/page/PageWrapper";
import CardError from "../../app/card/CardError";
import Card from '../../app/card/Card';
import { AllRolesResponse, ManagedUsersResponse, Role, User } from "../../data/exported";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import Text from '../../components/text/Text';
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import { Fragment, useState } from "react";
import SpacedColumn from "../../app/layout/SpacedColumn";
import { ModalBody, ModalState, ModalView, useModalState, ModalHeader, ModalActions } from "../../components/modal/Modal";
import GeneralButton from "../../components/button/GeneralButton";

interface RoleSelection {
    role: Role;
    selected: boolean;
};

const RolesLoadedRow = (props: {user: User, allRoles: Role[]}) => {
    const [checkedRoles, setCheckedRoles] = useState<RoleSelection[]>(
        props.allRoles.map(role => {
            return {
                role: role,
                selected: props.user.roles.map(role => role.id).includes(role.id),
            }; 
        })
    );

    const toggleRole = (role: Role) => {
        const newChecked = [...checkedRoles];
        newChecked.find(elem => elem.role.id === role.id)!!.selected = !newChecked.find(elem => elem.role.id === role.id)!!.selected;
        setCheckedRoles(newChecked);
    };

    return (
        <SpacedColumn rowGap={4}>
            {checkedRoles.map((selection: RoleSelection) => {
                return (
                    <div style={{display: 'flex', flexDirection: 'row'}} key={selection.role.id + '_' + selection.selected}>
                        <input type='checkbox' checked={selection.selected} onChange={() => toggleRole(selection.role)} />
                        <Text>{selection.role.id}</Text>
                    </div>
                );
            })}
        </SpacedColumn>
    );
};

const RolesRow = (props: {user: User}) => {
    const response = useAsyncRequest<AllRolesResponse>((controller: AbortController) => backendApi.roles(controller));
    if (response instanceof RequestSuccess) {
        return <RolesLoadedRow user={props.user} allRoles={response.result.roles} />;
    }

    if (response instanceof RequestError) {
        return (
            <CardError>
                <Text>{response.message}</Text>
            </CardError>
        );
    }

    return (
        <Card>
            <SpaceAroundRow>
                <Loader large />
            </SpaceAroundRow>
        </Card>
    );
};

const UserRolesModal = (props: {user: User, modalState: ModalState}) => {
    return (
        <ModalView state={props.modalState}>
            <ModalHeader>
                <Text large>Изменение ролей</Text>
            </ModalHeader>
            <ModalBody>
                <SpacedColumn rowGap={8}>
                    <Text>Логин: {props.user.login}</Text>
                    <Text>Имя: {props.user.name}</Text>
                    <Text>Фамилия: {props.user.surname}</Text>
                    <Text>Почта: {props.user.email}</Text>
                    <RolesRow user={props.user} />
                </SpacedColumn>
            </ModalBody>
            <ModalActions>
                <GeneralButton disabled>Сохранить</GeneralButton>
                <GeneralButton secondary onClick={() => props.modalState.close()}>Отмена</GeneralButton>
            </ModalActions>
        </ModalView>
    );
};

const UserRow = (props: {user: User}) => {
    const modalState = useModalState();
    return (
        <Fragment>
            <UserRolesModal modalState={modalState} user={props.user} />
            <SpaceBetweenRow onClick={() => modalState.open()}>
                <Text>{props.user.login}</Text>
                <Text>{props.user.name}</Text>
                <Text>{props.user.surname}</Text>
                <Text>{props.user.email}</Text>
            </SpaceBetweenRow>
        </Fragment>
    );
};

const UsersTable = (props: {users: User[]}) => {
    return (
        <Fragment>
            {props.users.map(user => {
                return (
                    <UserRow user={user} key={user.id} />
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

const AdminPageHeader = () => {
    return (
        <Card>
            <SpacedColumn rowGap={8}>
                <Text header>Администрирование</Text>
                <Text>На данной страничке можно изменять роли всех приглашенных пользователей</Text>
            </SpacedColumn>
        </Card>
    );
};

const AdminPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={16}>
                <AdminPageHeader />
                <AdminPageImpl />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default AdminPage;
