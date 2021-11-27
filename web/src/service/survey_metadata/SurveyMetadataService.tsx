import { action, makeObservable, observable } from 'mobx';
import apiService from '../../api/backendApiServiceSingleton';
import { SurveyMetadata } from '../../data/Survey';

interface SurveyMetadataProvider {
    metadata: SurveyMetadata | undefined;
    loading: boolean;
};

class SurveyMetadataProviderImpl implements SurveyMetadataProvider {
    surveyId_: number;

    metadata: SurveyMetadata | undefined;
    loading: boolean;
    
    constructor(surveyId: number) {
        this.metadata = undefined;
        this.loading = true;
        this.surveyId_ = surveyId;

        makeObservable(
            this,
            {
                metadata: observable,
                loading: observable,
                _setMetadata: action,
            }
        );

        this._fetchMetadata();
    }

    _fetchMetadata() {
        apiService.fetchMetadata(this.surveyId_)
            .then(metadata => {
                this._setMetadata(metadata);
            });
    }

    _setMetadata(meta: SurveyMetadata) {
        this.metadata = meta;
        this.loading = false;
    }
};

class SurveyMetadataService {
    createProvider(surveyId: number): SurveyMetadataProvider {
        return new SurveyMetadataProviderImpl(surveyId);
    }
};

const surveyMetadataService = new SurveyMetadataService();

export default surveyMetadataService;
export type {
    SurveyMetadata,
    SurveyMetadataProvider,
};
