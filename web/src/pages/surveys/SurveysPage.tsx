import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import styled from "styled-components";
import { Survey } from "../../data/exported";
import { observer } from "mobx-react-lite";
import surveysService, { SurveysError, SurveysLoaded, SurveysLoading, SurveysService, SurveysState } from "../../service/survey/SurveysService";
import Loader from "../../components/loader/Loader";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import GeneralButton from "../../components/button/GeneralButton";
import { Fragment, useState } from "react";
import { Navigate } from 'react-router-dom';
import surveyMetadataService, { SurveyMetadataProvider } from "../../service/survey_metadata/SurveyMetadataService";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpaceBetweenReversedRow from "../../app/layout/SpaceBetweenReversedRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import CardError from "../../app/card/CardError";

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

    const gotoDownloadAnswers = () => {  
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
                <GeneralButton onClick={gotoDownloadAnswers}>Выгрузить ответы</GeneralButton>
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
    const [expanded, setExpanded] = useState(false);
    const toggleMetadata = () => {
        setExpanded(!expanded);
    };

    return (
        <TableRow>
            <SpaceBetweenRow>
                <Text large style={{width: '300px'}}>{props.survey.name}</Text>
                <GeneralButton onClick={toggleMetadata} secondary>{expanded ? 'свернуть' : 'раскрыть'}</GeneralButton>
            </SpaceBetweenRow>
            { expanded && <SurveyMetadataRenderer survey={props.survey} /> }
        </TableRow>
    );
};

const SurveysTable = (props: {surveysState: SurveysState, isActivating: boolean}) => {
    if (props.surveysState instanceof SurveysLoading || props.isActivating) {
        return (
            <SpaceAroundRow>
                <Loader large />
            </SpaceAroundRow>
        );
    }

    if (props.surveysState instanceof SurveysError) {
        return (
            <CardError style={{marginTop: '16px'}}>
                <SpaceAroundRow>
                    <SpacedCenteredColumn rowGap={16}>
                        <Text large>Ошибка загрузки опросов, ошибка:</Text>
                        <Text>{props.surveysState.error}</Text>
                        <GeneralButton onClick={() => {surveysService._fetchSurveys()}}>Попробовать еще раз</GeneralButton>
                    </SpacedCenteredColumn>
                </SpaceAroundRow>
            </CardError>
        )
    }

    return (
        <Table>
            {
                (props.surveysState as SurveysLoaded).surveys.map(survey => {
                    return <SurveyRow survey={survey} key={survey.id} />;
                })
            }
        </Table>
    );
}

const SurveysWrapper = observer((props: {surveysService: SurveysService}) => {
    return <SurveysTable
        surveysState={props.surveysService.surveysState}
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
