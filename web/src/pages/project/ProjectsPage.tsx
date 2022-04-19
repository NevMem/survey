import PageWrapper from "../../app/page/PageWrapper";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import CardError from "../../app/card/CardError";
import { CreateProjectRequest, Project } from "../../data/exported";
import SpacedColumn from "../../app/layout/SpacedColumn";
import Text from "../../components/text/Text";
import Loader from "../../components/loader/Loader";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import GeneralButton from "../../components/button/GeneralButton";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import TextButton from "../../components/button/TextButton";
import { Fragment, useEffect, useState } from "react";
import { ModalActions, ModalBody, ModalHeader, ModalState, ModalView, useModalState } from "../../components/modal/Modal";
import Input from "../../components/input/Input";
import CardSuccess from "../../app/card/CardSuccess";
import ProjectCard from "./ProjectCard";

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

const ProjectsPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <ProjectPageHeader />
                <ProjectPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default ProjectsPage;
