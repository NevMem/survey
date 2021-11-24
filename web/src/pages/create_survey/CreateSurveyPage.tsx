import { observer } from 'mobx-react-lite';
import { Fragment, useState } from 'react';
import styled from 'styled-components';
import Text from '../../components/text/Text';
import createSurveyService, { CreateSurveyService } from '../../service/create_survey/CreateSurveyService';
import plusIcon from '../../images/base/plus.svg';
import GeneralButton from '../../components/button/GeneralButton';
import Question, { RatingQuestion } from '../../data/Question';

const PageWrapper = styled.div`
    margin: 20px;
`;

const WrappedRow = styled.div`
    padding: 20px;
    background-color: ${props => props.theme.secondaryBackgrond};
    border-radius: 8px;
    margin-top: 10px;
    margin-bottom: 10px;
`;

// const WrappedRow = styled.div`
//     padding: 20px;
//     background-color: ${props => props.theme.background};
//     border-radius: 8px;
//     margin-top: 10px;
//     margin-bottom: 10px;
//     border: 2px solid ${props => props.theme.secondaryBackgrond};
// `;

const Input = styled.input`
    outline: none;
    padding: 4px;
    padding-left: 10px;
    border: 2px solid ${props => props.theme.secondaryBackgrond};
    border-radius: 4px;
    background-color: ${props => props.theme.background};
    font-size: 1.5em;
`;

const Modal = styled.div`
    margin: 15% auto;
    width: 400px;
    background-color: ${props => props.theme.background};
    border-radius: 8px;
    padding: 16px;
    clickable: true;
    z-index: 2;
`;

const ModalHeader = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
`;

const ModalBody = styled.div`
    display: flex;
    flex-direction: column;
    padding-top: 20px;
    padding-bottom: 20px;
`;

const ModalActions = styled.div`
    display: flex;
    flex-direction: row-reverse;
    column-gap: 10px;
`;

const Selector = styled.select`
    outline: none;
    padding: 8px;
`;

const RatingQuestionBlock = (props: {setQuestion: (question: Question | undefined) => void}) => {
    const [min, setMin] = useState(0);
    const [max, setMax] = useState(10);
    const [name, setName] = useState('');

    const updateQuestion = (newMin?: number, newMax?: number, newName?: string) => {
        props.setQuestion({title: newName ?? name, min: newMin ?? min, max: newMax ?? max} as RatingQuestion);
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

const QuestionCreationBlock = (props: {type: string, setQuestion: (question: Question | undefined) => void}) => {
    if (props.type === '-') {
        return null;
    }
    if (props.type === 'rating') {
        return (
            <RatingQuestionBlock setQuestion={props.setQuestion} />
        );
    }
    return null;
}

const AddQuestionSection = () => {
    const [opened, setOpened] = useState(false);
    const openNewQuestionModal = () => {
        setOpened(true);
    }
    const closeNewQuestionModal = () => {
        setOpened(false);
    }

    const options = [
        '-', 'rating', 'stars', 'text',
    ]
    const [selectedOption, setSelectedOption] = useState(options[0]);

    const selectorChanged = (event: any) => {
        const newValue = event.target.value;
        setSelectedOption(newValue)
    }

    const [currentQuestion, setCurrentQuestion] = useState<Question | undefined>(undefined);

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
                        <Selector value={selectedOption} onChange={selectorChanged}>
                            {options.map((value) => {
                                return <option key={value}>{value}</option>;                                
                            })}
                        </Selector>
                        <QuestionCreationBlock type={selectedOption} setQuestion={setCurrentQuestion} />
                    </ModalBody>
                    <ModalActions>
                        <GeneralButton>Добавить</GeneralButton>
                        <GeneralButton secondary onClick={closeNewQuestionModal}>Отмена</GeneralButton>
                    </ModalActions>
                </Modal>
            </div>
            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'space-around', padding: '16px', cursor: 'pointer'}} onClick={openNewQuestionModal}>
                <img src={plusIcon} alt='plus icon' width='32px' height='32px' />
            </div>
        </Fragment>
    );
}

const NewSurveyBlock = observer((props: { createSurveyService: CreateSurveyService }) => {
    const nameChanged = (event: any) => {
        props.createSurveyService.setName(event.target.value)
    }

    const resetAll = () => {
        props.createSurveyService.reset()
    }

    return (
        <Fragment>
            <WrappedRow>
                <Text>Название опроса:</Text>
                <br/>
                <Input value={props.createSurveyService.name} onChange={nameChanged}></Input>
            </WrappedRow>
            <AddQuestionSection />
            <WrappedRow style={{display: 'flex', flexDirection: 'row-reverse', columnGap: '10px'}}>
                <GeneralButton>Создать опрос</GeneralButton> 
                <GeneralButton onClick={resetAll} secondary>Сбросить все</GeneralButton>
            </WrappedRow>
        </Fragment>
    );
})

const CreateSurveyPage = () => {
    return (
        <PageWrapper>
            <Text large>Создаем опрос</Text>
            <NewSurveyBlock createSurveyService={createSurveyService} />
        </PageWrapper>
    );
};

export default CreateSurveyPage;
