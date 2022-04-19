import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import useAsyncRequest, { RequestError, RequestSuccess } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import OutlinedCard from "../../app/card/OutlinedCard";
import CardError from "../../app/card/CardError";
import { Project, Survey } from "../../data/exported";
import SpacedColumn from "../../app/layout/SpacedColumn";
import Text from '../../components/text/Text';
import { Option, Select } from "../../components/select/Selector";
import { ChangeEvent, useState } from "react";

const SelectSurveyImpl = (props: { surveys: Survey[], selectSurvey: (survey: Survey | undefined) => void }) => {
    const { surveys } = props;

    const [selectedSurvey, setSelectedSurvey] = useState<Survey | undefined>();

    const notSelectedPlaceholder = '-';

    const handleChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const survey = surveys.find(survey => survey.name === event.target.value);
        props.selectSurvey(survey);
        setSelectedSurvey(survey);
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text large>Выберите опрос:</Text>
                <Select value={selectedSurvey?.name ?? notSelectedPlaceholder} onChange={handleChange}>
                    <Option>{notSelectedPlaceholder}</Option>
                    {surveys.map((survey, index) => {
                        return (
                            <Option key={index}>{survey.name}</Option>
                        );
                    })}
                </Select>
            </SpacedColumn>
        </OutlinedCard>
    );
};

const SelectSurvey = (props: { project: Project, selectSurvey: (survey: Survey | undefined) => void }) => {
    const request = useAsyncRequest(controller => backendApi.surveys(controller, props.project.id));

    if (request instanceof RequestError) {
        return (
            <CardError>{request.message}</CardError>
        );
    } else if (request instanceof RequestSuccess) {
        return (
            <SelectSurveyImpl surveys={request.result} selectSurvey={props.selectSurvey} />
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

const SelectorImpl = (props: { projects: Project[], selectSurvey: (survey: Survey | undefined) => void }) => {
    const { projects } = props;

    const [selectedProject, setSelectedProject] = useState<Project | undefined>();

    const notSelectedPlaceholder = '-';

    return (
        <SpacedColumn rowGap={16}>
            <OutlinedCard>
                <SpacedColumn rowGap={16}>
                    <Text large>Выберите проект:</Text>
                    <Select
                        value={selectedProject?.name ?? notSelectedPlaceholder}
                        onChange={event => setSelectedProject(projects.find(project => project.name === event.target.value))}
                    >
                        <Option>{notSelectedPlaceholder}</Option>
                        {projects.map((project, index) => {
                            return (
                                <Option key={index}>{project.name}</Option>
                            );
                        })}
                    </Select>
                </SpacedColumn>
            </OutlinedCard>

            {selectedProject !== undefined && <SelectSurvey project={selectedProject} selectSurvey={props.selectSurvey} />}
        </SpacedColumn>
    );
};

const NewSurveySelector = (props: { selectSurvey: (survey: Survey | undefined) => void }) => {
    const request = useAsyncRequest(controller => backendApi.projects(controller));

    if (request instanceof RequestError) {
        return (
            <CardError>{request.message}</CardError>
        );
    } else if (request instanceof RequestSuccess) {
        return (
            <SelectorImpl projects={request.result} selectSurvey={props.selectSurvey} />
        );
    }

    return (
        <OutlinedCard>
            <SpaceAroundRow>
                <Loader/>
            </SpaceAroundRow>
        </OutlinedCard>
    );
};

export default NewSurveySelector;
