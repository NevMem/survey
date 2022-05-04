import CardError from "../../app/card/CardError";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpacedColumn from "../../app/layout/SpacedColumn";
import GeneralButton from "../../components/button/GeneralButton";
import Loader from "../../components/loader/Loader";
import { Survey, Project, ProjectInfo } from "../../data/exported";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import Text from '../../components/text/Text';
import backendApi from '../../api/backendApiServiceSingleton';
import SpacedCenteredRow from "../../app/layout/SpacedCenteredRow";
import { useNavigate } from "react-router-dom";
import TextButton from "../../components/button/TextButton";
import styled from "styled-components";
import SpacedRow from "../../app/layout/SpacedRow";
import useNavigator from "../navigation";

const SurveyCard = styled.div`
    border-radius: 8px;
    background-color: ${props => props.theme.background};
    padding: 16px 24px;
    box-shadow: 0px 3px 8px ${props => props.theme.withAlpha(10).foreground};
    border: 2px solid ${props => props.theme.withAlpha(30).foreground};
    transition: all ease-in 0.1s;
    cursor: pointer;

    &:hover {
        box-shadow: 0px 3px 8px ${props => props.theme.withAlpha(60).foreground};
    }
`;

const SurveyView = (props: { survey: Survey }) => {
    const { survey } = props;

    const navigator = useNavigator();

    const gotoMoreInfo = () => {
        navigator.surveyInfoPage(survey.id);
    };

    return (
        <SurveyCard onClick={gotoMoreInfo}>
            <SpacedColumn rowGap={12}>
                <Text>{survey.name}</Text>
                <Text>{survey.surveyId}</Text>
            </SpacedColumn>
        </SurveyCard>
    );
};

const ProjectSurveysView = (props: { surveys: Survey[] }) => {
    const { surveys } = props;

    const navigate = useNavigate();

    const gotoCreateSurvey = () => {
        navigate("/create_survey");
    };

    if (surveys.length === 0) {
        return (
            // <OutlinedCard>
                <SpacedCenteredColumn rowGap={16}>
                    <Text>В данном проекте пока нет опросов</Text>
                    <GeneralButton onClick={gotoCreateSurvey}>Создать опрос</GeneralButton>
                </SpacedCenteredColumn>
            // </OutlinedCard>
        );
    }

    return (
        // <OutlinedCard>
            <SpacedColumn rowGap={24}>
                <Text>Опросы в проекте:</Text>
                <SpacedRow columnGap={16}>
                    {surveys.map((survey, index) => {
                        return (
                            <SurveyView key={index} survey={survey} />
                        );
                    })}
                </SpacedRow>
                <SpaceAroundRow>
                    <TextButton onClick={gotoCreateSurvey}>Добавить опрос</TextButton>
                </SpaceAroundRow>
            </SpacedColumn>
        // </OutlinedCard>
    );
};

const ProjectSurveysWrapper = (props: { project: Project }) => {
    const { project } = props;
    const request = useAsyncRequest(controller => backendApi.surveys(controller, project.id));

    if (request instanceof RequestError) {
        return (
            <CardError>
                <Text>{request.message}</Text>
            </CardError>
        );
    } else if (request instanceof RequestSuccess) {
        return <ProjectSurveysView surveys={request.result} />
    }
    return <SpaceAroundRow><Loader large/></SpaceAroundRow>
};

const ProjectInfoView = (props: { projectInfo: ProjectInfo }) => {
    return (
        <SpacedColumn rowGap={8}>
            <Text>Администраторы</Text>
            <OutlinedCard>
                <SpacedColumn rowGap={4}>
                    {props.projectInfo.administratorsInfo.map((info, index) => {
                        return (
                            <SpacedCenteredRow columnGap={16} key={index}>
                                <Text large>{info.administrator.name} {info.administrator.surname}</Text>
                                <Text>@{info.administrator.login}</Text>
                            </SpacedCenteredRow>
                        );
                    })}
                </SpacedColumn>
            </OutlinedCard>
        </SpacedColumn>
    );
};

const ProjectInfoWrapper = (props: { project: Project }) => {
    const { project } = props;
    const request = useAsyncRequest(controller => backendApi.projectInfo(controller, project.id));

    if (request instanceof RequestError) {
        return (
            <CardError>
                <Text>{request.message}</Text>
            </CardError>
        );
    } else if (request instanceof RequestSuccess) {
        return <ProjectInfoView projectInfo={request.result} />
    }
    return <SpaceAroundRow><Loader large/></SpaceAroundRow>
};

export {
    ProjectSurveysWrapper,
    ProjectInfoWrapper,
};
