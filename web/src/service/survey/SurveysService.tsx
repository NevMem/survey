import { makeObservable, observable, action } from 'mobx';
import { Survey, UnsavedSurvey } from '../../data/Survey';
import apiService from '../../api/backendApiServiceSingleton';

class SurveysService {
    surveys: Survey[]

    constructor() {
        this.surveys = [];
        makeObservable(
            this,
            {
                getSurveys: observable,
                _addSurveyImpl: action,
            }
        );
    }

    getSurveys(): Survey[] {
        return this.surveys;
    }

    addSurvey(unsavedSurvey: UnsavedSurvey) {
        apiService.addSurvey(unsavedSurvey)
            .then(survey => {
                this._addSurveyImpl(survey)
            })
    }

    _addSurveyImpl(survey: Survey) {
        this.surveys.push(survey)
    }
};

const surveysService = new SurveysService();

export default surveysService;
