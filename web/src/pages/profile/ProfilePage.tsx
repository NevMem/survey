import { useEffect, useState } from 'react';
import CardError from '../../app/card/CardError';
import SpaceAroundRow from '../../app/layout/SpaceAroundRow';
import PageWrapper from '../../app/page/PageWrapper';
import Loader from '../../components/loader/Loader';
import { Administrator } from '../../data/exported';
import authService from '../../service/authorization/AuthorizationService';
import Text from '../../components/text/Text';
import SpacedColumn from '../../app/layout/SpacedColumn';
import SpaceBetweenReversedRow from '../../app/layout/SpaceBetweenReversedRow';
import GeneralButton from '../../components/button/GeneralButton';
import OutlinedCard from '../../app/card/OutlinedCard';

const ProfileWrapper = (props: {user: Administrator | undefined, error: string | undefined, loading?: boolean}) => {
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
            <OutlinedCard>
                <SpacedColumn rowGap={16}>
                    <Text large>Имя: {user.name}</Text>
                    <Text large>Фамилия: {user.surname}</Text>
                    <Text large>Никнейм: {user.login}</Text>
                    <Text large>Почта: {user.email}</Text>
                </SpacedColumn>
                <SpaceBetweenReversedRow>
                    <GeneralButton secondary onClick={logout}>Выйти</GeneralButton>
                </SpaceBetweenReversedRow>
            </OutlinedCard>
        );
    }

    return null;
};

const ProfilePage = () => {
    const [user, setUser] = useState<Administrator | undefined>(undefined);
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
            <SpacedColumn rowGap={24}>
                <Text header>Профиль</Text>
                <ProfileWrapper user={user} error={error} loading={loading} />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default ProfilePage;
