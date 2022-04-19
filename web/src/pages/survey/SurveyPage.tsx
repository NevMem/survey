import { useParams } from "react-router-dom";
import SpacedColumn from "../../app/layout/SpacedColumn";
import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import useAsyncRequest, { isOk, RequestError, RequestState, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import OutlinedCard from "../../app/card/OutlinedCard";
import { Survey } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import CardError from "../../app/card/CardError";

const SurveyPageImpl = (props: { survey: Survey }) => {
    const { survey } = props;
    return (
        <OutlinedCard>
            <SpacedColumn rowGap={8}>
                <Text large>{survey.name}</Text>
            </SpacedColumn>
        </OutlinedCard>
    );
};

const SurveyPageContent = () => {
    const { id } = useParams();

    const request = useAsyncRequest(controller => backendApi.survey(controller, parseInt(id ?? "0")));

    if (isOk(request)) {
        return (
            <SurveyPageImpl survey={request.result} />
        );
    } else if (request instanceof RequestError) {
        return (
            <CardError>
                <Text>{request.message}</Text>
            </CardError>
        );
    }

    return (
        <OutlinedCard>
            <SpaceAroundRow>
                <Loader />
            </SpaceAroundRow>
        </OutlinedCard>
    );
};

const SurveyPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Опрос</Text>
                <SurveyPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default SurveyPage;
