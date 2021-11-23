import { observer } from 'mobx-react-lite';
import { Fragment, useState } from 'react';
import styled from 'styled-components';
import Text from '../../components/text/Text';
import createSurveyService, { CreateSurveyService } from '../../service/create_survey/CreateSurveyService';
import plusIcon from '../../images/base/plus.svg';
import GeneralButton from '../../components/button/GeneralButton';

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

const AddQuestionSection = () => {
    const [opened, setOpened] = useState(false);
    const openNewQuestionModal = () => {
        setOpened(true);
    }
    const closeNewQuestionModal = () => {
        setOpened(false);
    }

    const selectorChanged = (event: any) => {
        const newValue = event.target.value;
    }

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
                        <Selector onChange={selectorChanged}>
                            <option>-</option>
                            <option>a</option>
                            <option>b</option>
                            <option>c</option>
                        </Selector>
                    </ModalBody>
                    <ModalActions>
                        <GeneralButton>Ок</GeneralButton>
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
