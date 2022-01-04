import Sidebar from "../../components/sidebar/Sidebar";
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import pages from '../../pages/pages';
import { createContext, useContext } from "react";
import { User } from "../../data/exported";
import authorizationService, { AuthorizationService } from "../../service/authorization/AuthorizationService";
import { observer } from 'mobx-react';
import { useState, useEffect } from "react";
import Loader from "../../components/loader/Loader";
import Text from '../../components/text/Text';
import { isFeatureEnabled } from "../../service/experiments/utils";
import { useNewSidebar } from "../../service/experiments/experiments";

const AppSidebarItem = styled.p`
    font-size: 1.2em;
    padding-top: 10px;
    padding-bottom: 10px;
    padding-left: 8px;
    margin: 0px;
    color: ${props => props.theme.primary};
    cursor: pointer;
    text-decoration: none;
    transition: all ease-in 0.1s;
    border-radius: 8px;

    &:hover {
        background-color: ${props => props.theme.primary};
        color: ${props => props.theme.background};
    }
`;

interface UserState {};
class UserLoading implements UserState {};
class UserUnauthorized implements UserState {};
class UserLoaded implements UserState {
    user: User;
    constructor(user: User) {
        this.user = user;
    }
};

const UserContext = createContext<UserState>(new UserLoading());

const UserProvider = observer((props: { children: any, authService: AuthorizationService }) => {
    const [user, setUser] = useState<User | undefined>(undefined);
    const [error, setError] = useState<string | undefined>(undefined);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (props.authService.authorized) {
            props.authService.loadMe()
                .then(user => {
                    setLoading(false);
                    setUser(user);
                })
                .catch(error => {
                    setLoading(false);
                    setError(error + "");
                });
        }
    }, [props.authService.authorized]);

    if (!props.authService.authorized) {
        return (
            <UserContext.Provider value={new UserUnauthorized()}>
                {props.children}
            </UserContext.Provider>
        );
    }

    if (loading) {
        return (
            <UserContext.Provider value={new UserLoading()}>
                {props.children}
            </UserContext.Provider>
        );
    }

    if (user) {
        return (
            <UserContext.Provider value={new UserLoaded(user)}>
                {props.children}
            </UserContext.Provider>
        );
    }

    return (
        <UserContext.Provider value={new UserUnauthorized()}>
            {props.children}
        </UserContext.Provider>
    );
});

const SidebarWrapper = () => {
    const userContext = useContext(UserContext);

    if (userContext instanceof UserLoaded) {
        return (
            <Sidebar>
                {pages.filter(info => info.useInSidebar).map((info, index) => {
                    return <Link key={index} to={info.path}><AppSidebarItem>{info.name}</AppSidebarItem></Link>
                })}
            </Sidebar>
        );
    }

    if (userContext instanceof UserLoading) {
        return (
            <Sidebar>
                <Loader large />
            </Sidebar>
        );
    }

    return (
        <Sidebar>
            <Text large>Не авторизованы</Text>
        </Sidebar>
    );
};

const AppSidebar = () => {
    if (isFeatureEnabled(useNewSidebar)) {
        return (
            <UserProvider authService={authorizationService}>
                <SidebarWrapper />
            </UserProvider>
        )
    }
    return (
        <Sidebar>
            {pages.filter(info => info.useInSidebar).map((info, index) => {
                return <Link key={index} to={info.path}><AppSidebarItem>{info.name}</AppSidebarItem></Link>
            })}
        </Sidebar>
    );
}

export default AppSidebar;
