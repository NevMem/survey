import SpaceAroundRow from '../../app/layout/SpaceAroundRow';
import PageWrapper from '../../app/page/PageWrapper';
import surveysService, { SurveysService } from '../../service/survey/SurveysService';
import Loader from '../../components/loader/Loader';
import { observer } from 'mobx-react-lite';
import Card from '../../app/card/Card';
import { Option, Select } from '../../components/select/Selector';
import Text from '../../components/text/Text';
import SpacedColumn from '../../app/layout/SpacedColumn';

const SurveySelector = observer((props: {surveysService: SurveysService}) => {
    if (props.surveysService.fetchingSurveys) {
        return (
            <Card>
                <SpaceAroundRow>
                    <Loader large />
                </SpaceAroundRow>
            </Card>
        );
    }

    return (
        <Card>
            <SpacedColumn rowGap={16}>
                <Text large>Выберите опрос:</Text>
                <Select>
                    {props.surveysService.surveys.map(survey => {
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
