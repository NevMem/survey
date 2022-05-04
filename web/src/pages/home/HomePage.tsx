import OutlinedCard from '../../app/card/OutlinedCard';
import SpacedColumn from '../../app/layout/SpacedColumn';
import PageWrapper from '../../app/page/PageWrapper';
import Text from '../../components/text/Text';

const HomePage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Домашняя страница</Text>
                <OutlinedCard>

                </OutlinedCard>
            </SpacedColumn>
        </PageWrapper>
    );
};

export default HomePage;
