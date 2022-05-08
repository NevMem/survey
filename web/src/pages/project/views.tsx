import CardError from "../../app/card/CardError";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpacedCenteredColumn from "../../app/layout/SpacedCenteredColumn";
import SpacedColumn from "../../app/layout/SpacedColumn";
import GeneralButton from "../../components/button/GeneralButton";
import Loader from "../../components/loader/Loader";
import { Survey, Project, ProjectInfo, ProjectAdministratorInfo } from "../../data/exported";
import useAsyncRequest, { isOk, RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import Text from '../../components/text/Text';
import backendApi from '../../api/backendApiServiceSingleton';
import SpacedCenteredRow from "../../app/layout/SpacedCenteredRow";
import { useNavigate } from "react-router-dom";
import TextButton from "../../components/button/TextButton";
import styled from "styled-components";
import SpacedRow from "../../app/layout/SpacedRow";
import useNavigator from "../navigation";
import Badge from "../../components/badge/Badge";
import { ModalActions, ModalBody, ModalHeader, ModalView, useModalState } from "../../components/modal/Modal";
import { Fragment, useState } from "react";
import Row from "../../app/layout/Row";

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

const AnswerCoolDownBadge = (props: { survey: Survey }) => {
    const { surveyCoolDown } = props.survey;
    if (surveyCoolDown === undefined) {
        return (
            <Badge info>Единственный ответ</Badge>
        );
    }
    if (surveyCoolDown === -2) {
        return (
            <Badge info>Любое число ответов</Badge>
        );
    }
    const knownValues = [
        {text: 'Раз в минуту', value: 60 * 1000},
        {text: 'Раз в час', value: 60 * 60 * 1000},
        {text: 'Раз в день', value: 24 * 60 * 60 * 1000},
        {text: 'Раз в неделю', value: 7 * 24 * 60 * 60 * 1000},
    ];
    const value = knownValues.find(value => value.value === surveyCoolDown);
    if (value !== undefined) {
        return (
            <Badge info>{value.text}</Badge>
        );
    }
    return (
        <Badge error>Один ответ в {surveyCoolDown} ms</Badge>
    );
};

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
                <AnswerCoolDownBadge survey={survey} />
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
            <SpacedCenteredColumn rowGap={16}>
                <Text>В данном проекте пока нет опросов</Text>
                <GeneralButton onClick={gotoCreateSurvey}>Создать опрос</GeneralButton>
            </SpacedCenteredColumn>
        );
    }

    return (
        <SpacedColumn rowGap={24}>
            <SpacedColumn rowGap={8}>
                <Text>Опросы в проекте:</Text>
                <SpacedRow columnGap={16} style={{flexWrap: 'wrap', rowGap: '16px'}}>
                    {surveys.map((survey, index) => {
                        return (
                            <SurveyView key={index} survey={survey} />
                        );
                    })}
                </SpacedRow>
            </SpacedColumn>
            <SpaceAroundRow>
                <TextButton onClick={gotoCreateSurvey}>Добавить опрос</TextButton>
            </SpaceAroundRow>
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

const AdministratorRolesView = (props: { info: ProjectAdministratorInfo, project: Project }) => {
    const { info } = props;

    const modalState = useModalState();

    const SelectRolesView = () => {
        const [selectedRoles, setSelectedRoles] = useState<string[]>(info.roles.map(role => role.id));
        const [counter, setCounter] = useState(0);

        const request = useAsyncRequest(controller => backendApi.roles(controller));
        if (isOk(request)) {
            return (
                <Fragment>
                    {request.result.roles.map((role, index) => {
                        return (
                            <SpacedRow key={index + '-' + counter} columnGap={4}>
                                <input
                                    type='checkbox'
                                    checked={selectedRoles.find(selected => selected === role.id) !== undefined}
                                    onChange={
                                        event => {
                                            if (event.target.checked) {
                                                setSelectedRoles([...selectedRoles, role.id]);
                                            } else {
                                                setSelectedRoles([...selectedRoles.filter(selected => selected !== role.id)]);
                                            }
                                            setCounter(counter + 1);
                                        }
                                    }
                                    />
                                <Text>{role.id}</Text>
                            </SpacedRow>
                        );
                    })}
                </Fragment>
            );
        } else if (request instanceof RequestError) {
            return (
                <CardError>{request.message}</CardError>
            )
        }
        return (
            <SpaceAroundRow>
                <Loader />
            </SpaceAroundRow>
        );
    };

    return (
        <SpacedRow columnGap={12}>
            {info.roles.map((role, index) => {
                return (
                    <Badge success key={index}>{role.id}</Badge>
                );
            })}
            <TextButton onClick={() => modalState.toggle()}>Изменить роли</TextButton>
            <ModalView state={modalState}>
                <ModalHeader>
                    <Text large>Изменить роли {info.administrator.name} {info.administrator.surname} в проекте {props.project.name}</Text>
                </ModalHeader>
                <ModalBody>
                    <SelectRolesView />
                </ModalBody>
                <ModalActions>
                    <GeneralButton>Обновить</GeneralButton>
                    <TextButton>Отмена</TextButton>
                </ModalActions>
            </ModalView>
        </SpacedRow>
    );
};

const ProjectInfoView = (props: { projectInfo: ProjectInfo, project: Project }) => {
    return (
        <SpacedColumn rowGap={8}>
            <Text>Администраторы:</Text>
            <OutlinedCard>
                <SpacedColumn rowGap={8}>
                    {props.projectInfo.administratorsInfo.map((info, index) => {
                        return (
                            <SpacedCenteredRow columnGap={16} key={index}>
                                <Text large>{info.administrator.name} {info.administrator.surname}</Text>
                                <Text>@{info.administrator.login}</Text>
                                <AdministratorRolesView info={info} project={props.project} />
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
        return <ProjectInfoView projectInfo={request.result} project={project} />
    }
    return <SpaceAroundRow><Loader large/></SpaceAroundRow>
};

export {
    ProjectSurveysWrapper,
    ProjectInfoWrapper,
};
