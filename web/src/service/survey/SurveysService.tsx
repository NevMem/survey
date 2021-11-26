import { makeObservable, observable, action } from 'mobx';
import { Survey, UnsavedSurvey } from '../../data/Survey';
import apiService from '../../api/backendApiServiceSingleton';

class SurveysService {
    surveys: Survey[];
    addingSurvey: boolean;
    fetchingSurveys: boolean;
    activatingSurvey: boolean;

    constructor() {
        this.surveys = [];
        this.addingSurvey = false;
        this.fetchingSurveys = false;
        this.activatingSurvey = false;

        makeObservable(
            this,
            {
                addingSurvey: observable,
                fetchingSurveys: observable,
                surveys: observable,
                activatingSurvey: observable,
                activateSurvey: action,
                addSurvey: action,
                _addSurveyImpl: action,
                _fetchSurveys: action,
                _setSurveys: action,
                _setAddingSurvey: action,
                _setActivatingSurvey: action,
                _setFetchingSurveys: action,
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

    activateSurvey(id: number) {
        this.activatingSurvey = true;
        apiService.activateSurvey(id)
            .then(() => {
                this._setActivatingSurvey(false);
                this._fetchSurveys();
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

    _setActivatingSurvey(isActivating: boolean) {
        this.activatingSurvey = isActivating;
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
