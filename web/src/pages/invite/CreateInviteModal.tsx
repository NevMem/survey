import { useState, ChangeEvent } from "react";
import CardError from "../../app/card/CardError";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpacedColumn from "../../app/layout/SpacedColumn";
import GeneralButton from "../../components/button/GeneralButton";
import Loader from "../../components/loader/Loader";
import { ModalState, ModalView, ModalHeader, ModalBody, ModalActions } from "../../components/modal/Modal";
import { Select, Option } from "../../components/select/Selector";
import { CreateInviteRequest, CreateInviteResponse, Invite, Project } from "../../data/exported";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import Text from '../../components/text/Text';
import backendApi from '../../api/backendApiServiceSingleton';
import { useNotification } from "../../app/notification/NotificationProvider";
import Input from "../../components/input/Input";

const InviteCreationRequestBlock = (props: { requestBuilder: (abortController: AbortController) => Promise<CreateInviteResponse> }) => {
    const result = useAsyncRequest(props.requestBuilder);

    if (result instanceof RequestError) {
        return <CardError><Text>{result.message}</Text></CardError>;
    }

    if (result instanceof RequestSuccess) {
        const actual: CreateInviteResponse = result.result;
        return (
            <OutlinedCard>
                Создали инвайт: {actual.invite.id}
            </OutlinedCard>
        );
    }

    return (
        <SpaceAroundRow>
            <Loader />
        </SpaceAroundRow>
    );
};

const ProjectSelectorImpl = (props: { projects: Project[], selectProject: (project: Project | undefined) => void }) => {

    const notSelectedPlaceholder = '-';

    const [selectedProject, setSelectedProject] = useState<Project | undefined>();

    const handleChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const project = props.projects.find(project => project.name === event.target.value);
        setSelectedProject(project);
        props.selectProject(project);
    };

    return (
        <SpacedColumn rowGap={8}>
            <Text>Выберите проект, в который хотите пригласить:</Text>
            <Select
                value={selectedProject?.name ?? notSelectedPlaceholder}
                onChange={handleChange}
            >
                <Option>{notSelectedPlaceholder}</Option>
                {props.projects.map((project, index) => {
                    return (
                        <Option key={index}>{project.name}</Option>
                    );
                })}
            </Select>
        </SpacedColumn>
    );
};

const ProjectSelector = (props: { selectProject: (project: Project | undefined) => void }) => {
    const request = useAsyncRequest(controller => backendApi.projects(controller));

    if (request instanceof RequestError) {
        return (
            <CardError>{request.message}</CardError>
        );
    } else if (request instanceof RequestSuccess) {
        return (
            <ProjectSelectorImpl projects={request.result} selectProject={props.selectProject} />
        );
    }

    return (
        <OutlinedCard>
            <SpaceAroundRow>
                <Loader />
            </SpaceAroundRow>
        </OutlinedCard>
    );
};

const CreateInviteModal = (props: { state: ModalState }) => {
    const availableExpirationTimes = [
        {
            caption: '10 минут',
            seconds: 60 * 10,
        },
        {
            caption: '1 час',
            seconds: 60 * 60,
        },
        {
            caption: '6 часов',
            seconds: 60 * 60 * 6,
        },
        {
            caption: '1 сутки',
            seconds: 60 * 60 * 24,
        },
    ];

    const [option, setOption] = useState(availableExpirationTimes[0]);
    const [request, setRequest] = useState<CreateInviteRequest | undefined>(undefined);
    const [selectedProject, setSelectedProject] = useState<Project | undefined>(undefined);
    const [userLogin, setUserLogin] = useState('');

    const notification = useNotification();

    const createInvite = () => {
        if (selectedProject !== undefined) {
            const request: CreateInviteRequest = {
                expirationTimeSeconds: option.seconds,
                userLogin: userLogin,
                projectId: selectedProject.id,
            };
            setRequest(request);
        } else {
            setRequest(undefined);
            notification("Вы не выбрали проект", "", "error");
        }
    };

    const changeSelection = (event: ChangeEvent<HTMLSelectElement>) => {
        setOption(availableExpirationTimes.find(time => time.caption === event.target.value)!!)
    };

    return (
        <ModalView state={props.state}>
            <ModalHeader>
                <Text large>Создать инвайт</Text>
            </ModalHeader>
            <ModalBody>
                <SpacedColumn rowGap={16}>

                    <ProjectSelector selectProject={project => setSelectedProject(project)} />

                    <SpacedColumn rowGap={8}>
                        <Text>Выберите время истечения инвайта:</Text>
                        <Select onChange={changeSelection}>
                            {availableExpirationTimes.map(elem => {
                                return <Option key={elem.seconds}>{elem.caption}</Option>;
                            })}
                        </Select>
                    </SpacedColumn>

                    <SpacedColumn rowGap={8}>
                        <Text>Выберите логин пользователя:</Text>
                        <Input value={userLogin} onChange={event => setUserLogin(event.target.value)} />
                    </SpacedColumn>

                    {request && <InviteCreationRequestBlock requestBuilder={(controller: AbortController) => backendApi.createInvite(request, controller)} />}
                </SpacedColumn>
            </ModalBody>
            <ModalActions>
                <GeneralButton onClick={createInvite}>Создать</GeneralButton>
                <GeneralButton secondary onClick={() => props.state.close()}>Отмена</GeneralButton>
            </ModalActions>
        </ModalView>
    );
}

export default CreateInviteModal;
