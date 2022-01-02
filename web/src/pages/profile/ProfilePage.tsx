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
        return (
            <Card>
                <SpacedColumn rowGap={16}>
                    <Text header>Профиль</Text>
                    <Text large>{user.name}</Text>
                    <Text large>{user.surname}</Text>
                    <Text large>{user.login}</Text>
                </SpacedColumn>
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
