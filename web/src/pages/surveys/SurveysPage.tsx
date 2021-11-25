import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import styled from "styled-components";
import { Survey } from "../../data/Survey";
import { Fragment } from "react";
import { observer } from "mobx-react-lite";
import surveysService, { SurveysService } from "../../service/survey/SurveysService";

const TableRow = styled.div`
    display: flex;
    flex-direction: row;
    column-gap: 16px;
    padding: 8px;
    border: 2px solid ${props => props.theme.secondaryBackgrond};
    border-radius: 4px;
    margin-top: 4px;
    margin-bottom: 4px;
`;

const SurveyRow = (props: {survey: Survey}) => {
    return (
        <TableRow>
            <Text large>{props.survey.name}</Text>
        </TableRow>
    );
};

const SurveysTable = (props: {surveys: Survey[]}) => {
    return (
        <Fragment>
            {
                props.surveys.map(survey => {
                    return <SurveyRow survey={survey} key={survey.id} />;
                })
            }
        </Fragment>
    );
}

const SurveysWrapper = observer((props: {surveysService: SurveysService}) => {
    return <SurveysTable surveys={props.surveysService.surveys} />
});

const SurveysPage = () => {
    return (
        <PageWrapper>
            <Text large>Опросы</Text>
            <SurveysWrapper surveysService={surveysService} />
        </PageWrapper>
    );
};

export default SurveysPage;
