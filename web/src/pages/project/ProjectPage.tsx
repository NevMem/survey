import PageWrapper from "../../app/page/PageWrapper";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import OutlinedCard from "../../app/card/OutlinedCard";
import { Project, ProjectInfo, Survey } from "../../data/exported";
import SpacedColumn from "../../app/layout/SpacedColumn";
import Text from "../../components/text/Text";
import Loader from "../../components/loader/Loader";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpacedCenteredRow from "../../app/layout/SpacedCenteredRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";

const ProjectInfoView = (props: { projectInfo: ProjectInfo }) => {
    return (
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

const ProjectSurveysView = (props: { surveys: Survey[] }) => {
    const { surveys } = props;

    if (surveys.length === 0) {
        return (
            <SpacedCenteredColumn rowGap={8}>
                <Text>В данном проекте пока нет опросов</Text>
            </SpacedCenteredColumn>
        );
    }

    return (
        <SpacedColumn rowGap={8}>
            {surveys.map((survey, index) => {
                <Text key={index}>{survey.name}</Text>
            })}
        </SpacedColumn>
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

const ProjectCard = (props: { project: Project }) => {
    const { project } = props;

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text large>{project.name}</Text>
                <ProjectSurveysWrapper project={project} />
                <ProjectInfoWrapper project={project} />
            </SpacedColumn>
        </OutlinedCard>
    );
};

const ProjectPageImpl = () => {
    const request = useAsyncRequest(controller => backendApi.projects(controller));

    if (request instanceof RequestError) {
        return <CardError>{request.message}</CardError>
    }

    if (request instanceof RequestSuccess) {
        const projects: Project[] = request.result;
        return (
            <SpacedColumn rowGap={16}>
                {projects.map((elem, index) => {
                    return <ProjectCard project={elem} key={index} />
                })}
            </SpacedColumn>
        );
    }

    return (
        <SpaceAroundRow>
            <Loader large />
        </SpaceAroundRow>
    );
};

const ProjectPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Проекты</Text>
                <ProjectPageImpl />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default ProjectPage;
