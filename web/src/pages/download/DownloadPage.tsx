import SpaceAroundRow from '../../app/layout/SpaceAroundRow';
import PageWrapper from '../../app/page/PageWrapper';
import surveysService, { SurveysError, SurveysLoaded, SurveysLoading, SurveysService } from '../../service/survey/SurveysService';
import Loader from '../../components/loader/Loader';
import { observer } from 'mobx-react-lite';
import Card from '../../app/card/Card';
import { Option, Select } from '../../components/select/Selector';
import Text from '../../components/text/Text';
import SpacedColumn from '../../app/layout/SpacedColumn';
import CardError from '../../app/card/CardError';
import SpacedCenteredColumn from '../../app/layout/SpacedCenteredColumn';
import GeneralButton from '../../components/button/GeneralButton';
import { Survey, Task, TaskState } from '../../data/exported';
import { ChangeEvent, useState } from 'react';
import useAsyncRequest, { RequestError, RequestSuccess } from '../../utils/useAsyncUtils';
import backendApi from '../../api/backendApiServiceSingleton';
import Badge from '../../components/badge/Badge';
import usePollingRequest, { PollingError, PollingState, PollingSuccess } from '../../utils/usePollingRequest';
import Row from '../../app/layout/Row';
import TaskView from '../../app/task/TaskView';

const SurveySelector = observer((props: {surveysService: SurveysService, selectSurvey: (survey: Survey | undefined) => void}) => {
    if (props.surveysService.surveysState instanceof SurveysLoading) {
        return (
            <Card>
                <SpaceAroundRow>
                    <Loader large />
                </SpaceAroundRow>
            </Card>
        );
    }

    if (props.surveysService.surveysState instanceof SurveysError) {
        return (
            <CardError style={{marginTop: '16px'}}>
                <SpaceAroundRow>
                    <SpacedCenteredColumn rowGap={16}>
                        <Text large>Ошибка загрузки опросов, ошибка:</Text>
                        <Text>{props.surveysService.surveysState.error}</Text>
                        <GeneralButton onClick={() => {surveysService.fetchSurveys()}}>Попробовать еще раз</GeneralButton>
                    </SpacedCenteredColumn>
                </SpaceAroundRow>
            </CardError>
        );
    }

    const loadedSurveys = surveysService.surveysState as SurveysLoaded;

    const selectorChanged = (event: ChangeEvent<HTMLSelectElement>) => {
        const survey = loadedSurveys.surveys.find(survey => survey.name === event.target.value);
        props.selectSurvey(survey);
    };

    return (
        <Card>
            <SpacedColumn rowGap={16}>
                <Text large>Выберите опрос:</Text>
                <Select onChange={selectorChanged}>
                    <Option key='null-opt'>-</Option>
                    {loadedSurveys.surveys.map(survey => {
                        return (
                            <Option key={survey.id}>{survey.name}</Option>
                        );
                    })}
                </Select>
            </SpacedColumn>
        </Card>
    );
});

/* const RatingQuestionFilter = (props: {question: RatingQuestion, index: number}) => {
    const [active, setActive] = useState(false);

    const [filterMin, setFilterMin] = useState(props.question.min);
    const [filterMax, setFilterMax] = useState(props.question.max);

    const changeFilterMin = (event: ChangeEvent<HTMLInputElement>) => {
        setFilterMin((event.target.value as any) | 0);
    };

    const changeFilterMax = (event: ChangeEvent<HTMLInputElement>) => {
        setFilterMax((event.target.value as any) | 0);
    };

    return (
        <Card>
            <SpacedColumn rowGap={8}>
                <Text large>Вопрос {props.index}: {props.question.title}</Text>
                <div>
                    {!active && <GeneralButton onClick={() => {setActive(true)}}>Активировать фильтр</GeneralButton>}
                    {active && <GeneralButton secondary onClick={() => {setActive(false)}}>Деактивировать фильтр</GeneralButton>}
                </div>

                {active && <Fragment>
                        <Text>Минимум</Text>
                        <Input value={filterMin} onChange={changeFilterMin} />
                        <Text>Максимум</Text>
                        <Input value={filterMax} onChange={changeFilterMax} />
                    </Fragment>
                }
            </SpacedColumn>
        </Card>
    );
};

const QuestionFilter = (props: {question: Question, index: number}) => {
    if (instanceOfRatingQuestion(props.question)) {
        return <RatingQuestionFilter question={props.question} index={props.index} />
    }
    return null;
}; */

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

// const TaskLogsView = (props: {task: Task}) => {
//     return (
//         <SpacedColumn rowGap={4}>
//             {props.task.log.map((elem, index) => {
//                 return <Text key={index}>{elem.timestamp}:{elem.message}</Text>;
//             })}
//         </SpacedColumn>
//     );
// };

// const TaskOutputsView = (props: {task: Task}) => {
//     return (
//         <SpaceAroundRow>
//             {props.task.outputs.map((elem, index) => {
//                 return (
//                     <a key={index} href={elem.url}>{elem.filename}</a>
//                 );
//             })}
//         </SpaceAroundRow>
//     );
// };

// const TaskView = (props: {task: Task}) => {
//     const { task } = props;

//     if (task.state === TaskState.Waiting) {
//         return (
//             <Row>
//                 <Loader small/>
//                 <Text>{task.state}</Text>
//             </Row>
//         );
//     }

//     if (task.state === TaskState.Success) {
//         return (
//             <SpacedCenteredColumn rowGap={8}>
//                 <Badge success>{task.state}</Badge>
//                 <TaskLogsView task={task}/>
//                 <TaskOutputsView task={task} />
//             </SpacedCenteredColumn>
//         );
//     }

//     return (
//         <SpacedCenteredColumn rowGap={8}>
//             <Badge warning>{task.state}</Badge>
//             <TaskLogsView task={task}/>
//         </SpacedCenteredColumn>
//     );
// };


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
            <TaskView task={request.result} />
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
                <SurveySelector surveysService={surveysService} selectSurvey={(survey) => {setSelectedSurvey(survey)}} />

                <SurveyDownloadDataFilter survey={selectedSurvey} />
                <SurveyDownloadDataBlock survey={selectedSurvey} />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default DownloadPage;
