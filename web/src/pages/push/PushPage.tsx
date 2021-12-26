import CardError from "../../app/card/CardError";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import PageWrapper from "../../app/page/PageWrapper";
import Text from '../../components/text/Text';

const PushPage = () => {
    return (
        <PageWrapper>
            <CardError>
                <SpaceAroundRow><Text large>Эта страница все еще в разработке</Text></SpaceAroundRow>
            </CardError>
        </PageWrapper>
    )
}

export default PushPage;
