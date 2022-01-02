import { useEffect, useState } from 'react';
import CardError from '../../app/card/CardError';
import SpaceAroundRow from '../../app/layout/SpaceAroundRow';
import PageWrapper from '../../app/page/PageWrapper';
import Loader from '../../components/loader/Loader';
import { User } from '../../data/exported';
import authService from '../../service/authorization/AuthorizationService';
import Text from '../../components/text/Text';
import Card from '../../app/card/Card';
import SpacedColumn from '../../app/layout/SpacedColumn';
import Badge from '../../components/badge/Badge';
import SpaceBetweenReversedRow from '../../app/layout/SpaceBetweenReversedRow';
import GeneralButton from '../../components/button/GeneralButton';

const ProfileWrapper = (props: {user: User | undefined, error: string | undefined, loading?: boolean}) => {
    if (props.loading) {
        return (
            <SpaceAroundRow>
                <Loader large />
            </SpaceAroundRow>
        );
    }

    if (props.error !== undefined) {
        return (
            <CardError>
                <Text large>{props.error}</Text>
            </CardError>
        );
    }

    if (props.user !== undefined) {
        const user = props.user;

        const logout = () => {
            authService.logout();
        };

        return (
            <Card>
                <SpacedColumn rowGap={16}>
                    <Text header>Профиль</Text>
                    <Text large>Имя: {user.name}</Text>
                    <Text large>Фамилия: {user.surname}</Text>
                    <Text large>Никнейм: {user.login}</Text>
                    <Text large>Почта: {user.email}</Text>
                    <div style={{display: 'flex', flexDirection: 'row', alignItems: 'center', columnGap: '16px'}}>
                        <Text>Роли:</Text>
                        {user.roles.map((role, index) => {
                            return <Badge success key={index}>{role.id}</Badge>;
                        })}
                    </div>
                </SpacedColumn>
                <SpaceBetweenReversedRow>
                    <GeneralButton secondary onClick={logout}>Выйти</GeneralButton>
                </SpaceBetweenReversedRow>
            </Card>
        );
    }

    return null;
};

const ProfilePage = () => {
    const [user, setUser] = useState<User | undefined>(undefined);
    const [error, setError] = useState<string | undefined>(undefined);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        authService.loadMe()
            .then(user => {
                setLoading(false);
                setUser(user);
            })
            .catch(error => {
                setLoading(false);
                setError(error + "");
            })
    }, [])

    return (
        <PageWrapper>
            <ProfileWrapper user={user} error={error} loading={loading} />
        </PageWrapper>
    );
};

export default ProfilePage;
