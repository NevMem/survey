import { observer } from 'mobx-react-lite';
import { Fragment } from 'react';
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

const AddQuestionSection = () => {
    return (
        <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'space-around', padding: '16px', cursor: 'pointer'}}>
            <img src={plusIcon} alt='plus icon' width='32px' height='32px' />
        </div>
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
