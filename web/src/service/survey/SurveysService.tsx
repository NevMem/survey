import { makeObservable, observable, action } from 'mobx';
import { Survey, UnsavedSurvey } from '../../data/Survey';
import apiService from '../../api/backendApiServiceSingleton';
import { NotificationAction } from '../../app/notification/data';

class SurveysState {};
class SurveysLoaded extends SurveysState {
    surveys: Survey[];

    constructor(surveys: Survey[]) {
        super();
        this.surveys = surveys;
    }
};
class SurveysLoading extends SurveysState {
};
class SurveysError extends SurveysState {
    error: string;

    constructor(error: string) {
        super();
        this.error = error;
    }
}

class SurveysService {
    surveys: Survey[];
    surveysState: SurveysState;
    addingSurvey: boolean;
    fetchingSurveys: boolean;
    activatingSurvey: boolean;
    notificationUser?: (title: string, text: string, type?: string, actions?: NotificationAction[]) => string

    constructor() {
        this.surveys = [];
        this.addingSurvey = false;
        this.fetchingSurveys = false;
        this.activatingSurvey = false;
        this.surveysState = new SurveysLoading();

        makeObservable(
            this,
            {
                addingSurvey: observable,
                fetchingSurveys: observable,
                surveys: observable,
                activatingSurvey: observable,
                surveysState: observable,
                activateSurvey: action,
                addSurvey: action,
                _addSurveyImpl: action,
                _fetchSurveys: action,
                _setSurveys: action,
                _setAddingSurvey: action,
                _setActivatingSurvey: action,
                _setFetchingSurveys: action,
                _setSurveysState: action,
            }
        );

        this._fetchSurveys();
    }
    
    setNotificationUser(notificationUser: (title: string, text: string, type?: string, actions?: NotificationAction[]) => string) {
        this.notificationUser = notificationUser;
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
        this._setSurveysState(new SurveysLoading());
        apiService.fetchSurveys()
            .then(surveys => {
                this._setSurveys(surveys);
                this._setFetchingSurveys(false);
                
                this._setSurveysState(new SurveysLoaded(surveys));
            })
            .catch(error => {
                console.log(error);
                this.notificationUser?.('Ошибка загрузки опросов', error);

                this._setSurveysState(new SurveysError(error));
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

    _setSurveysState(surveysState: SurveysState) {
        this.surveysState = surveysState;
    }
};

const surveysService = new SurveysService();

export default surveysService;

export type {
    SurveysService,
};

export {
    SurveysState,
    SurveysLoaded,
    SurveysError,
    SurveysLoading,
};
