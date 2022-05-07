import { observer } from 'mobx-react-lite';
import { ChangeEvent, Fragment, useEffect, useState } from 'react';
import styled from 'styled-components';
import Text from '../../components/text/Text';
import createSurveyService, { CreateSurveyService } from '../../service/create_survey/CreateSurveyService';
import plusIcon from '../../images/base/plus.svg';
import GeneralButton from '../../components/button/GeneralButton';
import { instanceOfRatingQuestion, instanceOfStarsQuestion, instanceOfTextQuestion, Question, TextQuestion, StarsQuestion, RatingQuestion, Project, RadioQuestion, instanceOfRadioQuestion } from '../../data/exported';
import PageWrapper from '../../app/page/PageWrapper';
import { UnsavedSurvey } from '../../data/Survey';
import surveysService, { SurveysService } from '../../service/survey/SurveysService';
import Loader from '../../components/loader/Loader';
import SpaceAroundRow from '../../app/layout/SpaceAroundRow';
import { commonQuestions, commonQuestionTitle } from '../../data/commonQuestions';
import { CommonQuestion } from '../../data/CommonQuestion';
import Input from '../../components/input/Input';
import Modal, { ModalHeader, ModalBody, ModalActions } from '../../components/modal/Modal';
import OutlinedCard from '../../app/card/OutlinedCard';
import SpacedColumn from '../../app/layout/SpacedColumn';
import TextButton from '../../components/button/TextButton';
import useAsyncRequest, { RequestError, RequestSuccess } from '../../utils/useAsyncUtils';
import backendApi from '../../api/backendApiServiceSingleton';
import { Option, Select } from '../../components/select/Selector';
import SpaceBetweenRow from '../../app/layout/SpaceBetweenRow';
import deleteIcon from '../../images/base/delete.svg';
import Column from '../../app/layout/Column';

const Selector = styled.select`
    outline: none;
    padding: 8px;
`;

const RatingQuestionBlock = (props: {setQuestion: (question: Question | undefined) => void}) => {
    const [min, setMin] = useState(0);
    const [max, setMax] = useState(10);
    const [name, setName] = useState('');

    const updateQuestion = (newMin?: number, newMax?: number, newName?: string) => {
        props.setQuestion({type: "rating", title: newName ?? name, min: newMin ?? min, max: newMax ?? max} as RatingQuestion);
    }

    const changeMin = (event: any) => {
        setMin(event.target.value | 0);
        updateQuestion(event.target.value | 0);
    }

    const changeMax = (event: any) => {
        setMax(event.target.value | 0);
        updateQuestion(undefined, event.target.value | 0);
    }

    const changeName = (event: any) => {
        setName(event.target.value);
        updateQuestion(undefined, undefined, event.target.value);
    }

    return (
        <div style={{paddingTop: '10px', paddingBottom: '10px'}}>
            <Text>Вопрос:</Text>
            <Input value={name} onChange={changeName} />
            <Text>Минимальный рейтинг:</Text>
            <Input value={min} onChange={changeMin} />
            <Text>Максимальный рейтинг:</Text>
            <Input value={max} onChange={changeMax} />
        </div>
    );
}

const TextQuestionBlock = (props: {setQuestion: (question: Question | undefined) => void}) => {
    const [name, setName] = useState('');
    const [maxLength, setMaxLength] = useState(20);

    const changeName = (event: any) => {
        setName(event.target.value);
        props.setQuestion({ type: "text", title: event.target.value, maxLength: maxLength } as TextQuestion);
    }

    const changeMaxLength = (event: any) => {
        setMaxLength(event.target.value | 0);
        props.setQuestion({ type: "text", title: name, maxLength: event.target.value | 0 } as TextQuestion);
    }

    return (
        <div style={{paddingTop: '10px', paddingBottom: '10px'}}>
            <Text>Вопрос:</Text>
            <Input value={name} onChange={changeName} />
            <Text>Максимальная длина ответа:</Text>
            <Input value={maxLength} onChange={changeMaxLength} />
        </div>
    );
}


const StarsQuestionBuilderBlock = (props: {setQuestion: (question: Question | undefined) => void}) => {
    const [name, setName] = useState('');
    const [starsCount, setStarsCount] = useState(5);

    const changeName = (event: any) => {
        setName(event.target.value);
        props.setQuestion({ type: "stars", title: event.target.value, stars: starsCount } as StarsQuestion);
    }

    const changeStarsCount = (event: any) => {
        setStarsCount(event.target.value | 0);
        props.setQuestion({ type: "stars", title: name, stars: event.target.value | 0 } as StarsQuestion);
    }

    return (
        <div style={{paddingTop: '10px', paddingBottom: '10px'}}>
            <Text>Вопрос:</Text>
            <Input value={name} onChange={changeName} />
            <Text>Количество звезд:</Text>
            <Input value={starsCount} onChange={changeStarsCount} />
        </div>
    );
}

const RadioQuestionBuilderBlock = (props: { setQuestion: (question: Question | undefined) => void }) => {
    const [variants, setVariants] = useState<string[]>([]);

    const [variant, setVariant] = useState('');

    const [title, setTitle] = useState('');

    const deleteVariant = (index: number) => {
        const newVarinats = [...variants];
        newVarinats.splice(index, 1)
        setVariants(newVarinats);
    };
    
    const addVariant = () => {
        if (variant.length !== 0) {
            const newVarinats = [...variants, variant];
            setVariants(newVarinats);
            setVariant('');
        }
    };

    useEffect(() => {
        if (variants.length === 0) {
            props.setQuestion(undefined);
        } else {
            const question: RadioQuestion = {
                type: 'radio',
                title: title,
                variants: variants.map((variant, index) => {return { id: index + '', variant: variant }}),
            };
            props.setQuestion(question);
        }
    }, [variants]);

    return (
        <SpacedColumn rowGap={12}>
            <SpacedColumn rowGap={4}>
                <Text>Название:</Text>
                <Input value={title} onChange={event => setTitle(event.target.value)} />
            </SpacedColumn>

            <Text>Варианты ответа:</Text>
            {variants.map((elem, index) => {
                return (
                    <SpaceBetweenRow key={index}>
                        <Text>{elem}</Text>
                        <img onClick={() => deleteVariant(index)} src={deleteIcon} alt='delete' width='24px' style={{cursor: 'pointer'}} />
                    </SpaceBetweenRow>
                );
            })}
            <SpaceBetweenRow>
                <Input value={variant} onChange={event => setVariant(event.target.value)} />
                <img onClick={addVariant} src={plusIcon} alt='plus' width='24px' style={{cursor: 'pointer'}} />
            </SpaceBetweenRow>
        </SpacedColumn>
    );
};

const QuestionCreationBlock = (props: {type: string, setQuestion: (question: Question | undefined) => void}) => {
    switch (props.type) {
        case 'rating':
            return <RatingQuestionBlock setQuestion={props.setQuestion} />;
        case 'text':
            return <TextQuestionBlock setQuestion={props.setQuestion} />;
        case 'stars':
            return <StarsQuestionBuilderBlock setQuestion={props.setQuestion} />;
        case 'radio':
            return <RadioQuestionBuilderBlock setQuestion={props.setQuestion} />;
    }
    return null;
}

const AddQuestionSection = (props: { createSurveyService: CreateSurveyService }) => {
    const [opened, setOpened] = useState(false);
    const openNewQuestionModal = () => {
        setOpened(true);
    }
    const closeNewQuestionModal = () => {
        setOpened(false);
    }

    const options = [
        '-',
        'rating',
        'stars',
        'text',
        'radio',
    ]
    const [selectedOption, setSelectedOption] = useState(options[0]);

    const selectorChanged = (event: any) => {
        const newValue = event.target.value;
        setSelectedOption(newValue)
    }

    const [currentQuestion, setCurrentQuestion] = useState<Question | undefined>(undefined);

    const saveQuestionIfPossible = () => {
        if (currentQuestion) {
            props.createSurveyService.addQuestion(currentQuestion);
        }
    }

    const [canSave, setCanSave] = useState(false);

    useEffect(() => {
        setCanSave(currentQuestion !== undefined);
    }, [currentQuestion]);

    return (
        <Fragment>
            <div style={{
                display: opened ? 'block': 'none',
                position: 'fixed',
                zIndex: '1',
                left: '0',
                top: '0',
                right: '0',
                bottom: '0',
                backgroundColor: 'rgba(0, 0, 0, 0.2)'
            }} onClick={closeNewQuestionModal}>
                <Modal onClick={(event) => {event.preventDefault(); event.stopPropagation();}}>
                    <ModalHeader>
                        <Text>Новый опрос</Text>
                        <div>&times;</div>
                    </ModalHeader>
                    <ModalBody>
                        <SpacedColumn rowGap={16}>
                            <Selector value={selectedOption} onChange={selectorChanged}>
                                {options.map((value) => {
                                    return <option key={value}>{value}</option>;                                
                                })}
                            </Selector>
                            <QuestionCreationBlock type={selectedOption} setQuestion={setCurrentQuestion} />
                        </SpacedColumn>
                    </ModalBody>
                    <ModalActions>
                        <GeneralButton onClick={saveQuestionIfPossible} disabled={!canSave}>Добавить</GeneralButton>
                        <GeneralButton secondary onClick={closeNewQuestionModal}>Отмена</GeneralButton>
                    </ModalActions>
                </Modal>
            </div>
            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'space-around', padding: '16px', cursor: 'pointer'}} onClick={openNewQuestionModal}>
                <img src={plusIcon} alt='plus icon' width='32px' height='32px' />
            </div>
        </Fragment>
    );
};

const QuestionBlock = (props: {question: Question, onDelete: () => void}) => {
    const { question } = props;
    if (instanceOfRatingQuestion(question)) {
        return (
            <OutlinedCard>
                <SpacedColumn rowGap={8}>
                    <Text large>Вопрос с рейтингом:</Text>
                    <Text>{question.title}</Text>
                    <Text large>Значения от {question.min} до {question.max}</Text>
                </SpacedColumn>
            </OutlinedCard>
        );
    }
    if (instanceOfStarsQuestion(question)) {
        return (
            <OutlinedCard>
                <SpacedColumn rowGap={8}>
                    <Text large>Вопрос с рейтингом в виде звезд:</Text>
                    <Text>{question.title}</Text>
                    <Text large>Количество звезд: {question.stars}</Text>
                </SpacedColumn>
            </OutlinedCard>
        );
    }
    if (instanceOfTextQuestion(question)) {
        return (
            <OutlinedCard>
                <SpacedColumn rowGap={8}>
                    <Text large>Текстовый вопрос:</Text>
                    <Text>{question.title}</Text>
                    <Text large>Максимальная длина ответа: {question.maxLength}</Text>
                </SpacedColumn>
            </OutlinedCard>
        );
    }
    if (instanceOfRadioQuestion(question)) {
        return (
            <OutlinedCard>
                <SpacedColumn rowGap={8}>
                    <SpaceBetweenRow>
                        <Text large>Вопрос с единственным выбором:</Text>
                        <TextButton onClick={props.onDelete}>Удалить</TextButton>
                    </SpaceBetweenRow>
                    <Text>{question.title}</Text>
                    <Text>Варианты:</Text>
                    {question.variants.map((elem, index) => {
                        return (
                            <Text key={index}>{elem.variant}</Text>
                        );
                    })}
                </SpacedColumn>
            </OutlinedCard>
        );
    }
    
    return null;
};

const SelectProjectBlockImpl = (props: { projects: Project[], setSelectedProject: (project?: Project) => void }) => {
    const { projects, setSelectedProject } = props;
    const [selectedValue, setSelectedValue] = useState('-');

    const selectorChanged = (event: ChangeEvent<HTMLSelectElement>) => {
        const value = event.target.value;

        const selectedProject = projects.find(project => project.name === value);

        setSelectedProject(selectedProject);

        setSelectedValue(value);
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text large>Выберите проект в котором нужно создать опрос:</Text>
                <Select onChange={selectorChanged} value={selectedValue}>
                    <Option>-</Option>
                    {projects.map((elem: Project, index: number) => {
                        return (
                            <Option key={index}>{elem.name}</Option>
                        );
                    })}
                </Select>
            </SpacedColumn>
        </OutlinedCard>
    );
};

const SelectProjectBlock = (props: { setSelectedProject: (project?: Project) => void }) => {
    const response = useAsyncRequest(controller => backendApi.projects(controller));

    if (response instanceof RequestError) {
        return (
            <OutlinedCard>{response.message}</OutlinedCard>
        );
    } else if (response instanceof RequestSuccess) {
        return (
            <SelectProjectBlockImpl projects={response.result} setSelectedProject={props.setSelectedProject} />
        );
    }

    return (
        <OutlinedCard>
            <SpaceAroundRow>
                <Loader large />
            </SpaceAroundRow>
        </OutlinedCard>
    );
};

const SelectAnswerCoolDownBlock = (props: { selectedCoolDown: number, setSelectedCoolDown: (selectedCoolDown: number) => void }) => {
    const values = [
        {text: 'Можно отвечать сколько угодно раз', value: -2},
        {text: 'Единоразовый ответ', value: -1},
        {text: 'Отвечать не чаще чем раз в минуту', value: 60 * 1000},
        {text: 'Отвечать не чаще чем раз в час', value: 60 * 60 * 1000},
        {text: 'Отвечать не чаще чем раз в день', value: 24 * 60 * 60 * 1000},
        {text: 'Отвечать не чаще чем раз в неделю', value: 7 * 24 * 60 * 60 * 1000},
    ];

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text large>Пожалуйста выберите насколько часто можно отвечать на опрос:</Text>
                <Select
                    value={values.find(value => value.value === props.selectedCoolDown)?.text ?? '-'}
                    onChange={event => props.setSelectedCoolDown(values.find(value => value.text === event.target.value)?.value ?? -1)}
                >
                    {values.map((value, index) => {
                        return (
                            <Option key={index}>{value.text}</Option>
                        );
                    })}
                </Select>
            </SpacedColumn>
        </OutlinedCard>  
    );
};

const NewSurveyBlock = observer((props: { createSurveyService: CreateSurveyService, surveysService: SurveysService }) => {
    const nameChanged = (event: any) => {
        props.createSurveyService.setName(event.target.value)
    };

    const resetAll = () => {
        props.createSurveyService.reset()
    };

    const [selectedCommonQuestions, setSelectedCommonQuestions] = useState(commonQuestions);

    const [selectedProject, setSelectedProject] = useState<Project | undefined>(undefined);

    const [answerCoolDown, setAnswerCoolDown] = useState(-1);

    const [canCreate, setCanCreate] = useState(false);

    useEffect(() => {
        setCanCreate(selectedProject !== undefined);
    }, [selectedProject]);

    const createSurvey = () => {
        const unsavedSurvey: UnsavedSurvey = {
            projectId: selectedProject!.id,
            name: props.createSurveyService.name,
            questions: props.createSurveyService.questions,
            commonQuestions: selectedCommonQuestions,
            answerCoolDown: answerCoolDown,
        };
        props.surveysService.addSurvey(unsavedSurvey);
    };

    if (props.surveysService.addingSurvey) {
        return (
            <Fragment>
                <Text large>Опрос добавляется</Text>
                <SpaceAroundRow>
                    <Loader large />
                </SpaceAroundRow>
            </Fragment>
        );
    }

    const toggleSelectedCommonQuestion = (commonQuestion: CommonQuestion) => {
        if (selectedCommonQuestions.find(question => question === commonQuestion)) {
            setSelectedCommonQuestions(
                selectedCommonQuestions.filter(question => question !== commonQuestion)
            );
            return;
        }
        setSelectedCommonQuestions([...selectedCommonQuestions, commonQuestion]);
    }

    return (
        <Fragment>
            <SpacedColumn rowGap={16}>
                <SelectProjectBlock setSelectedProject={(project) => setSelectedProject(project)} />
                <OutlinedCard>
                    <Text large>Название опроса:</Text>
                    <br/>
                    <Column>
                        <Input value={props.createSurveyService.name} onChange={nameChanged}></Input>
                    </Column>
                </OutlinedCard>
                <SelectAnswerCoolDownBlock selectedCoolDown={answerCoolDown} setSelectedCoolDown={value => setAnswerCoolDown(value)} />
                <OutlinedCard>
                    <SpacedColumn rowGap={8}>
                        <Text large>Общие вопросы:</Text>
                        <SpacedColumn rowGap={4}>
                            {commonQuestions.map(question => {
                                return (
                                    <div key={question.id}>
                                        <label htmlFor={'question-' + question.id}>{commonQuestionTitle(question)}</label>
                                        <input
                                            type='checkbox'
                                            id={'question-' + question.id}
                                            checked={selectedCommonQuestions.find(q => q === question) !== undefined}
                                            onChange={() => {toggleSelectedCommonQuestion(question)}}
                                            />
                                    </div>
                                );
                            })}
                        </SpacedColumn>
                    </SpacedColumn>
                </OutlinedCard>

                {props.createSurveyService.questions.map((question, index) => {
                    return (
                        <QuestionBlock question={question} key={index} onDelete={() => props.createSurveyService.deleteQuestion(question)} />
                    );
                })}
                <AddQuestionSection createSurveyService={props.createSurveyService} />
                <OutlinedCard style={{display: 'flex', flexDirection: 'row-reverse', columnGap: '10px'}}>
                    <GeneralButton onClick={createSurvey} disabled={!canCreate}>Создать опрос</GeneralButton> 
                    <TextButton onClick={resetAll} secondary>Сбросить все</TextButton>
                </OutlinedCard>
            </SpacedColumn>
        </Fragment>
    );
})

const CreateSurveyPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Создать опрос</Text>
                <NewSurveyBlock createSurveyService={createSurveyService} surveysService={surveysService} />
            </SpacedColumn>
        </PageWrapper>
    );
};

export default CreateSurveyPage;
