import { observer } from 'mobx-react-lite';
import { Fragment } from 'react';
import styled from 'styled-components';
import Text from '../../components/text/Text';
import createSurveyService, { CreateSurveyService } from '../../service/create_survey/CreateSurveyService';

const PageWrapper = styled.div`
    margin: 20px;
`;

const WrappedRow = styled.div`
    padding: 20px;
    background-color: ${props => props.theme.secondaryBackgrond};
    border-radius: 8px;
    margin-top: 10px;
    margin-bottom: 10px;
    box-shadow: 0px 2px 4px ${props => props.theme.secondaryBackgrond};
`;

const Input = styled.input`
    outline: none;
    padding: 4px;
    padding-left: 10px;
    border-radius: 4px;
    border: none;
    background-color: ${props => props.theme.background};
    font-size: 1.5em;
`;

const NewSurveyBlock = observer((props: { createSurveyService: CreateSurveyService }) => {
    const nameChanged = (event: any) => {
        props.createSurveyService.setName(event.target.value)
    }

    return (
        <Fragment>
            <WrappedRow>
                <Text>Название опроса:</Text>
                <br/>
                <Input value={props.createSurveyService.name} onChange={nameChanged}></Input>
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
