import { useParams } from "react-router-dom";
import SpacedColumn from "../../app/layout/SpacedColumn";
import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import useAsyncRequest, { isOk, RequestError } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import OutlinedCard from "../../app/card/OutlinedCard";
import { CommonQuestion, Question, Survey } from "../../data/exported";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import CardError from "../../app/card/CardError";
import { useState } from "react";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import TextButton from "../../components/button/TextButton";

const CommonQuestionCard = (props: { commonQuestion: CommonQuestion }) => {
    return (
        <OutlinedCard>
            <Text>Общий вопрос с идентификатором {props.commonQuestion.id}</Text>
        </OutlinedCard>
    );
};

const QuestionCard = (props: { question: Question }) => {
    return (
        <OutlinedCard>
            <Text>Вопрос с типом {props.question.type}</Text>
        </OutlinedCard>
    );
};

const SurveysQuestionsDescribeBlock = (props: { survey: Survey }) => {
    const { questions, commonQuestions } = props.survey;
    
    return (
        <SpacedColumn rowGap={8}>
            {commonQuestions.map((question, index) => {
                return (
                    <CommonQuestionCard key={index} commonQuestion={question} />
                );
            })}
            {questions.map((question, index) => {
                return <QuestionCard key={index} question={question} />;
            })}
        </SpacedColumn>
    );
};

const SurveyQuestionsBlock = (props: { survey: Survey }) => {
    const { survey } = props;

    const [expanded, setExpanded] = useState(false);

    return (
        <SpacedColumn rowGap={4}>
            <SpaceBetweenRow>
                <Text>Количество вопросов: {survey.commonQuestions.length + survey.questions.length}</Text>
                <TextButton onClick={() => setExpanded(!expanded)}>Просмотреть</TextButton>
            </SpaceBetweenRow>

            {expanded && <SurveysQuestionsDescribeBlock survey={survey} />}
        </SpacedColumn>
    );
};

const SurveyMetadataView = (props: { survey: Survey }) => {
    const { survey } = props;
    const request = useAsyncRequest(controller => backendApi.fetchMetadata(controller, survey.id));

    if (isOk(request)) {
        return (
            <SpacedColumn rowGap={4}>
                <Text>Получено {request.result.answersCount} ответов</Text>
            </SpacedColumn>
        );
    } else if (request instanceof RequestError) {
        return (
            <CardError>{request.message}</CardError>
        );
    }

    return (
        <SpaceAroundRow>
            <Loader />
        </SpaceAroundRow>
    );
};

const SurveyCoolDownInfoView = (props: { survey: Survey }) => {
    const { surveyCoolDown } = props.survey;

    const knownValues = [
        {text: 'На данный вопрос можно отвечать сколько угодно раз', value: -2},
        {text: 'На данный вопрос можно ответить только один раз', value: undefined},
        {text: 'На данный вопрос можно отвечать раз в минуту', value: 60 * 1000},
        {text: 'На данный вопрос можно отвечать раз в час', value: 60 * 60 * 1000},
        {text: 'На данный вопрос можно отвечать раз в день', value: 24 * 60 * 60 * 1000},
        {text: 'На данный вопрос можно отвечать раз в неделю', value: 7 * 24 * 60 * 60 * 1000},
    ];
    const value = knownValues.find(value => value.value === surveyCoolDown);
    if (value !== undefined) {
        return (
            <Text small>{value.text}</Text>
        );
    }
    return (
        <Text small>Отвечать можно раз в {surveyCoolDown} ms</Text>
    );
};

const SurveyPageImpl = (props: { survey: Survey }) => {
    const { survey } = props;
    return (
        <OutlinedCard>
            <SpacedColumn rowGap={24}>
                <SpacedColumn rowGap={8}>
                    <Text large>{survey.name}</Text>
                    <SurveyCoolDownInfoView survey={survey} />
                </SpacedColumn>
                <SurveyQuestionsBlock survey={survey} />
                <SurveyMetadataView survey={survey} />
            </SpacedColumn>
        </OutlinedCard>
    );
};

const SurveyPageContent = () => {
    const { id } = useParams();

    const request = useAsyncRequest(controller => backendApi.survey(controller, parseInt(id ?? "0")));

    if (isOk(request)) {
        return (
            <SurveyPageImpl survey={request.result} />
        );
    } else if (request instanceof RequestError) {
        return (
            <CardError>
                <Text>{request.message}</Text>
            </CardError>
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

const SurveyPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Опрос</Text>
                <SurveyPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default SurveyPage;
