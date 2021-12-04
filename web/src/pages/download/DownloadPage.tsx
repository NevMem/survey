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

const SurveySelector = observer((props: {surveysService: SurveysService}) => {
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

    return (
        <Card>
            <SpacedColumn rowGap={16}>
                <Text large>Выберите опрос:</Text>
                <Select>
                    {(props.surveysService.surveysState as SurveysLoaded).surveys.map(survey => {
                        return (
                            <Option key={survey.id}>{survey.name}</Option>
                        );
                    })}
                </Select>
            </SpacedColumn>
        </Card>
    );
});

const DownloadPage = () => {
    return (
        <PageWrapper>
            <Text header>Выгрузка данных опроса</Text>
            <SurveySelector surveysService={surveysService} />
        </PageWrapper>
    );
};

export default DownloadPage;
