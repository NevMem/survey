import PageWrapper from "../../app/page/PageWrapper";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import OutlinedCard from "../../app/card/OutlinedCard";
import { CreateProjectRequest, Project, ProjectInfo, Survey } from "../../data/exported";
import SpacedColumn from "../../app/layout/SpacedColumn";
import Text from "../../components/text/Text";
import Loader from "../../components/loader/Loader";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpacedCenteredRow from "../../app/layout/SpacedCenteredRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import GeneralButton from "../../components/button/GeneralButton";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import TextButton from "../../components/button/TextButton";
import { Fragment, useEffect, useState } from "react";
import { ModalActions, ModalBody, ModalHeader, ModalState, ModalView, useModalState } from "../../components/modal/Modal";
import Input from "../../components/input/Input";
import CardSuccess from "../../app/card/CardSuccess";

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

    const [expanded, setExpanded] = useState(false);

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <SpaceBetweenRow>
                    <Text large>{project.name}</Text>
                    <TextButton onClick={() => setExpanded(!expanded)}>{expanded ? 'Свернуть' : 'Развернуть'}</TextButton>
                </SpaceBetweenRow>
                {expanded && <ProjectSurveysWrapper project={project} />}
                {expanded && <ProjectInfoWrapper project={project} />}
            </SpacedColumn>
        </OutlinedCard>
    );
};

const ProjectPageContent = () => {
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

const CreateProjectRequestView = (props: { request: CreateProjectRequest }) => {
    const response = useAsyncRequest(controller => backendApi.createProject(controller, props.request));

    if (response instanceof RequestError) {
        return (
            <CardError>
                {response.message}
            </CardError>
        );
    } else if (response instanceof RequestSuccess) {
        return (
            <CardSuccess>Создали проект. Перезагрузите страницу чтобы увидеть изменения</CardSuccess>
        );
    }

    return (
        <SpaceAroundRow>
            <Loader large />
        </SpaceAroundRow>
    );
};

const CreateProjectModalContent = (props: { state: ModalState }) => {
    const [name, setName] = useState('');
    const [canCreate, setCanCreate] = useState(false);

    const [request, setRequest] = useState<CreateProjectRequest | undefined>();

    useEffect(() => {
        setCanCreate(name.length !== 0);
    }, [name]);

    const create = () => {
        setRequest({ name: name });
    };

    return (
        <Fragment>
            <ModalHeader>
                <Text large>Создать проект</Text>
            </ModalHeader>
            <ModalBody>
                <SpacedColumn rowGap={8}>
                    <Input value={name} onChange={ev => setName(ev.target.value)} placeholder='Имя проекта' />
                    {request !== undefined && <CreateProjectRequestView request={request} />}
                </SpacedColumn>
            </ModalBody>
            <ModalActions>
                <GeneralButton onClick={create} disabled={!canCreate}>Создать</GeneralButton>
                <TextButton onClick={() => props.state.close()}>Отмена</TextButton>
            </ModalActions>
        </Fragment>
    );
};

const ProjectPageHeader = () => {
    const modalState = useModalState();

    return (
        <Fragment>
            <ModalView state={modalState}>
                <CreateProjectModalContent state={modalState} />
            </ModalView>
            <SpaceBetweenRow>
                <Text header>Проекты</Text>
                <TextButton onClick={() => modalState.open()}>Создать проект</TextButton>
            </SpaceBetweenRow>
        </Fragment>
    );
};

const ProjectPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <ProjectPageHeader />
                <ProjectPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default ProjectPage;
