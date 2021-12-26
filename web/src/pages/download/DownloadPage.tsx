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
import { Survey } from '../../data/exported';
import { ChangeEvent, Fragment, useState } from 'react';

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
                        <GeneralButton onClick={() => {surveysService._fetchSurveys()}}>Попробовать еще раз</GeneralButton>
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
        <Fragment>
            {/*props.survey.questions.map((question, index) => {
                return <QuestionFilter question={question} key={question.title} index={index} />
            })*/}
        </Fragment>
    );
};

const DownloadPage = () => {
    const [selectedSurvey, setSelectedSurvey] = useState<Survey | undefined>(undefined);

    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Выгрузка данных опроса</Text>
                <SurveySelector surveysService={surveysService} selectSurvey={(survey) => {setSelectedSurvey(survey)}} />

                <SurveyDownloadDataFilter survey={selectedSurvey} />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default DownloadPage;
