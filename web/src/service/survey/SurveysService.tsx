import { makeObservable, observable, action } from 'mobx';
import { Survey, UnsavedSurvey } from '../../data/Survey';
import apiService from '../../api/backendApiServiceSingleton';

class SurveysService {
    surveys: Survey[];
    addingSurvey: boolean;
    fetchingSurveys: boolean;

    constructor() {
        this.surveys = [];
        this.addingSurvey = false;
        this.fetchingSurveys = false;

        makeObservable(
            this,
            {
                addingSurvey: observable,
                fetchingSurveys: observable,
                surveys: observable,
                addSurvey: action,
                _addSurveyImpl: action,
                _fetchSurveys: action,
                _setFetchingSurveys: action,
                _setSurveys: action,
                _setAddingSurvey: action,
            }
        );

        this._fetchSurveys();
    }

    addSurvey(unsavedSurvey: UnsavedSurvey) {
        this.addingSurvey = true;
        apiService.addSurvey(unsavedSurvey)
            .then(survey => {
                this._addSurveyImpl(survey);
                this._setAddingSurvey(false);
            });
    }

    _setAddingSurvey(isAdding: boolean) {
        this.addingSurvey = isAdding;
    }

    _addSurveyImpl(survey: Survey) {
        this.surveys.push(survey);
    }

    _fetchSurveys() {
        this.fetchingSurveys = true;
        apiService.fetchSurveys()
            .then(surveys => {
                this._setSurveys(surveys);
                this._setFetchingSurveys(false);
            });
    }
    
    _setSurveys(surveys: Survey[]) {
        this.surveys = surveys;
    }

    _setFetchingSurveys(isFetching: boolean) {
        this.fetchingSurveys = isFetching;
    }
};

const surveysService = new SurveysService();

export default surveysService;

export type {
    SurveysService
};
