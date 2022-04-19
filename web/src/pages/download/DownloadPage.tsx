import SpaceAroundRow from '../../app/layout/SpaceAroundRow';
import PageWrapper from '../../app/page/PageWrapper';
import surveysService from '../../service/survey/SurveysService';
import Loader from '../../components/loader/Loader';
import Card from '../../app/card/Card';
import Text from '../../components/text/Text';
import SpacedColumn from '../../app/layout/SpacedColumn';
import CardError from '../../app/card/CardError';
import GeneralButton from '../../components/button/GeneralButton';
import { Survey, Task, TaskState } from '../../data/exported';
import { useState } from 'react';
import useAsyncRequest, { RequestError, RequestSuccess } from '../../utils/useAsyncUtils';
import backendApi from '../../api/backendApiServiceSingleton';
import usePollingRequest, { PollingError, PollingState, PollingSuccess } from '../../utils/usePollingRequest';
import TaskView from '../../app/task/TaskView';
import SurveySelector from './SurveySelector';

const SurveyDownloadDataFilter = (props: {survey?: Survey}) => {
    if (!props.survey) {
        return (
            <Card>
                <Text large>Выберете опрос из селектора выше</Text>
            </Card>
        );
    }

    return (
        <CardError>
            <SpaceAroundRow>
                <Text large>Выгрузка данных с фильтрацией пока не доступна</Text>
            </SpaceAroundRow>
            <SpaceAroundRow>
                <Text>Вы можете выгрузить данные без фильтрации по кнопке ниже</Text>
            </SpaceAroundRow>
        </CardError>
    )
};

const PollingTaskView = (props: {task: Task}) => {
    const request = usePollingRequest(
        (controller: AbortController) => {
            return backendApi.loadTask({id: props.task.id}, controller);
        },
        (state: PollingState) => {
            if (state instanceof PollingSuccess && state.result.state === TaskState.Success) {
                return true;
            }
            return false;
        }
    );

    if (request instanceof PollingError) {
        return (
            <CardError>
                {request.message}
            </CardError>
        );
    }
    
    if (request instanceof PollingSuccess) {
        return (
            <TaskView task={request.result} expandedDefault={true} />
        );
    }

    return (
        <Loader />
    );
};

const ExportTaskBlock = (props: {survey: Survey}) => {
    const request = useAsyncRequest((controller: AbortController) => {
        return backendApi.createExportDataTask({surveyId: props.survey.id}, controller);
    });

    if (request instanceof RequestError) {
        return (
            <CardError>
                <SpaceAroundRow><Text large>{request.message}</Text></SpaceAroundRow>
            </CardError>
        );
    }

    if (request instanceof RequestSuccess) {
        return (
            <PollingTaskView task={request.result} />
        );
    }

    return (
        <SpaceAroundRow>
            <Loader />
        </SpaceAroundRow>
    )
};

const SurveyDownloadDataBlock = (props: {survey?: Survey}) => {
    const [download, setDownload] = useState(false);
    const [requestIndex, setRequestIndex] = useState(0);

    if (!props.survey) {
        return null;
    }

    const { survey } = props;

    const startDownloading = () => {
        setRequestIndex(requestIndex + 1);
        setDownload(true);
    };

    return (
        <SpacedColumn rowGap={16}>
            <SpaceAroundRow>
                <GeneralButton onClick={startDownloading}>Выгрузить</GeneralButton>
            </SpaceAroundRow>
            {download && <ExportTaskBlock survey={survey} key={requestIndex} />}
        </SpacedColumn>
    );
};

const DownloadPage = () => {
    const [selectedSurvey, setSelectedSurvey] = useState<Survey | undefined>(undefined);

    surveysService.prefetchSurveysIfNeeded();

    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Выгрузка данных опроса</Text>
                <SurveySelector selectSurvey={(survey) => {setSelectedSurvey(survey)}} />

                <SurveyDownloadDataFilter survey={selectedSurvey} />
                <SurveyDownloadDataBlock survey={selectedSurvey} />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default DownloadPage;
