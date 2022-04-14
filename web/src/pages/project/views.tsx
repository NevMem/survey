import CardError from "../../app/card/CardError";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpacedColumn from "../../app/layout/SpacedColumn";
import GeneralButton from "../../components/button/GeneralButton";
import Loader from "../../components/loader/Loader";
import { Survey, Project, ProjectInfo } from "../../data/exported";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import Text from '../../components/text/Text';
import backendApi from '../../api/backendApiServiceSingleton';
import SpacedCenteredRow from "../../app/layout/SpacedCenteredRow";

const ProjectSurveysView = (props: { surveys: Survey[] }) => {
    const { surveys } = props;

    if (surveys.length === 0) {
        return (
            <OutlinedCard>
                <SpacedCenteredColumn rowGap={16}>
                    <Text>В данном проекте пока нет опросов</Text>
                    <GeneralButton>Создать опрос</GeneralButton>
                </SpacedCenteredColumn>
            </OutlinedCard>
        );
    }

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text>Опросы проекта:</Text>
                <SpacedColumn rowGap={8}>
                    {surveys.map((survey, index) => {
                        return (
                            <SpaceBetweenRow key={index}>
                                <Text>{survey.name}</Text>
                                <Text>id: {survey.id}</Text>
                            </SpaceBetweenRow>
                        );
                    })}
                </SpacedColumn>
            </SpacedColumn>
        </OutlinedCard>
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
                            <SpacedCenteredRow columnGap={16}>
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
