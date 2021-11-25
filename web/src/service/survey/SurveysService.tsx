import { makeObservable, observable, action } from 'mobx';
import { Survey, UnsavedSurvey } from '../../data/Survey';
import apiService from '../../api/backendApiServiceSingleton';

class SurveysService {
    surveys: Survey[]
    addingSurvey: boolean

    constructor() {
        this.surveys = [];
        this.addingSurvey = false;
        makeObservable(
            this,
            {
                getSurveys: observable,
                addSurvey: action,
                _addSurveyImpl: action,
                isAddingSurvey: observable,
            }
        );
    }

    isAddingSurvey(): boolean {
        return this.addingSurvey;
    }

    getSurveys(): Survey[] {
        return this.surveys;
    }

    addSurvey(unsavedSurvey: UnsavedSurvey) {
        this.addingSurvey = true;
        apiService.addSurvey(unsavedSurvey)
            .then(survey => {
                this._addSurveyImpl(survey)
            });
    }

    _addSurveyImpl(survey: Survey) {
        this.surveys.push(survey);
    }
};

const surveysService = new SurveysService();

export default surveysService;
