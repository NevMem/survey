import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import styled from "styled-components";
import { Survey } from "../../data/Survey";
import { observer } from "mobx-react-lite";
import surveysService, { SurveysService } from "../../service/survey/SurveysService";
import Loader from "../../components/loader/Loader";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import Badge from "../../components/badge/Badge";
import GeneralButton from "../../components/button/GeneralButton";
import { Fragment, useState } from "react";
import { Navigate } from 'react-router-dom';
import surveyMetadataService, { SurveyMetadataProvider } from "../../service/survey_metadata/SurveyMetadataService";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpaceBetweenReversedRow from "../../app/layout/SpaceBetweenReversedRow";

const TableRow = styled.div`
    padding: 16px;

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

const MetadataContainer = styled.div`
    background-color: ${props => props.theme.secondaryBackground};
    padding: 16px;
    border-radius: 16px;
    margin-top: 12px;
    display: flex;
    flex-direction: column;
    row-gap: 12px;
`;

const MetadataWrapper = observer((props: {provider: SurveyMetadataProvider}) => {
    if (props.provider.loading) {
        return <SpaceAroundRow><Loader large/></SpaceAroundRow>
    }

    const gotoUploadAnswers = () => {  
    };

    return (
        <Fragment>
            <SpaceBetweenRow>
                <Text>Ответов: </Text>
                <Text>{props.provider.metadata?.answers}</Text>
            </SpaceBetweenRow>
            <SpaceBetweenRow>
                <Text>Файлов: </Text>
                <Text>{props.provider.metadata?.files}</Text>
            </SpaceBetweenRow>
            <SpaceBetweenReversedRow>
                <GeneralButton onClick={gotoUploadAnswers}>Выгрузить ответы</GeneralButton>
            </SpaceBetweenReversedRow>
        </Fragment>
    );
});

const SurveyMetadataRenderer = (props: {survey: Survey}) => {
    return (
        <MetadataContainer>
            <MetadataWrapper provider={surveyMetadataService.createProvider(props.survey.id)} />
        </MetadataContainer>
    );
};

const SurveyRow = (props: {survey: Survey}) => {
    const activateSurvey = () => {
        surveysService.activateSurvey(props.survey.id);
    };

    const [expanded, setExpanded] = useState(false);
    const toggleMetadata = () => {
        setExpanded(!expanded);
    };

    return (
        <TableRow>
            <SpaceBetweenRow>
                <Text large style={{width: '300px'}}>{props.survey.name}</Text>
                <Badge success={props.survey.active}>{props.survey.active ? 'Активный' : 'Отключен'}</Badge>
                <GeneralButton onClick={activateSurvey} disabled={props.survey.active}>Активировать</GeneralButton>
                <GeneralButton onClick={toggleMetadata} secondary>{expanded ? 'свернуть' : 'раскрыть'}</GeneralButton>
            </SpaceBetweenRow>
            { expanded && <SurveyMetadataRenderer survey={props.survey} /> }
        </TableRow>
    );
};

const SurveysTable = (props: {surveys: Survey[], isLoading: boolean, isActivating: boolean}) => {
    if (props.isLoading || props.isActivating) {
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
    return <SurveysTable
        surveys={props.surveysService.surveys}
        isLoading={props.surveysService.fetchingSurveys}
        isActivating={props.surveysService.activatingSurvey}
        />
});

const SurveysPage = () => {
    const [redirect, setRedirect]  = useState(false);
    const initRedirect = () => {
        setRedirect(true);
    };
    return (
        <PageWrapper>
            { redirect && <Navigate to="/create_survey" /> }
            <SpaceBetweenRow>
                <Text header>Опросы</Text>
                <GeneralButton onClick={initRedirect}>Создать опрос</GeneralButton>
            </SpaceBetweenRow>
            <SurveysWrapper surveysService={surveysService} />
        </PageWrapper>
    );
};

export default SurveysPage;
