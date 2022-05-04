import SpacedColumn from "../../app/layout/SpacedColumn";
import PageWrapper from "../../app/page/PageWrapper";
import Text from '../../components/text/Text';
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import { useParams } from "react-router-dom";
import { Project } from "../../data/exported";
import ProjectCard from "./ProjectCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";

const ProjectPageContent = () => {
    const { id } = useParams();
    const request = useAsyncRequest(controller => backendApi.project(controller, parseInt(id!!)));

    if (request instanceof RequestError) {
        return (
            <CardError>
                {request.message}
            </CardError>
        );
    } else if (request instanceof RequestSuccess) {
        const project: Project = request.result;
        return (
            <ProjectCard project={project} richCard />
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
                <Text header>Проект</Text>
                <ProjectPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default ProjectPage;
