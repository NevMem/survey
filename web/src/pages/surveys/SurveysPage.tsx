import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import styled from "styled-components";
import { Survey } from "../../data/Survey";
import { observer } from "mobx-react-lite";
import surveysService, { SurveysService } from "../../service/survey/SurveysService";
import Loader from "../../components/loader/Loader";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";

const TableRow = styled.div`
    display: flex;
    flex-direction: row;
    column-gap: 16px;
    padding: 16px;
    justify-content: space-between;

    &:not(:last-child) {
        border-bottom: 2px solid ${props => props.theme.secondaryBackground};
    }
`;

const Table = styled.div`
    display: flex;
    flex-direction: column;
    margin-top: 16px;
    margin-bottom: 16px;
    border: 2px solid ${props => props.theme.secondaryBackground};
    border-radius: 4px;
`;

const LoaderRow = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-around;
`;

const SurveyRow = (props: {survey: Survey}) => {
    return (
        <TableRow>
            <SpaceBetweenRow>
                <Text large>{props.survey.name}</Text>
            </SpaceBetweenRow>
        </TableRow>
    );
};

const SurveysTable = (props: {surveys: Survey[], isLoading: boolean}) => {
    if (props.isLoading) {
        return (
            <LoaderRow>
                <Loader large />
            </LoaderRow>
        );
    }

    return (
        <Table>
            {
                props.surveys.map(survey => {
                    return <SurveyRow survey={survey} key={survey.id} />;
                })
            }
        </Table>
    );
}

const SurveysWrapper = observer((props: {surveysService: SurveysService}) => {
    return <SurveysTable surveys={props.surveysService.surveys} isLoading={props.surveysService.fetchingSurveys} />
});

const SurveysPage = () => {
    return (
        <PageWrapper>
            <Text header>Опросы</Text>
            <SurveysWrapper surveysService={surveysService} />
        </PageWrapper>
    );
};

export default SurveysPage;
