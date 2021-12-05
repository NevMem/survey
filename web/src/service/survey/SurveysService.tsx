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
    surveysState: SurveysState;
    addingSurvey: boolean;
    activatingSurvey: boolean;
    notificationUser?: (title: string, text: string, type?: string, actions?: NotificationAction[]) => string

    constructor() {
        this.addingSurvey = false;
        this.activatingSurvey = false;
        this.surveysState = new SurveysLoading();

        makeObservable(
            this,
            {
                addingSurvey: observable,
                activatingSurvey: observable,
                surveysState: observable,
                activateSurvey: action,
                addSurvey: action,
                _fetchSurveys: action,
                _setAddingSurvey: action,
                _setActivatingSurvey: action,
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
                this._setAddingSurvey(false);
                this._fetchSurveys();
            })
            .catch(error => {
                this.notificationUser?.('Ошибка добавлениия опроса', error, 'error');
            });
    }

    activateSurvey(id: number) {
        this.activatingSurvey = true;
        apiService.activateSurvey(id)
            .then(() => {
                this._setActivatingSurvey(false);
                this._fetchSurveys();
            })
            .catch(error => {
                this.notificationUser?.('Ошибка активации опроса', error, 'error');
            });
    }

    _setAddingSurvey(isAdding: boolean) {
        this.addingSurvey = isAdding;
    }

    _fetchSurveys() {
        this._setSurveysState(new SurveysLoading());
        apiService.fetchSurveys()
            .then(surveys => {
                this._setSurveysState(new SurveysLoaded(surveys));
            })
            .catch(error => {
                this.notificationUser?.('Ошибка загрузки опросов', error, 'error', [{message: 'Еще раз', action: () => { this._fetchSurveys() } }]);

                this._setSurveysState(new SurveysError(error));
            });
    }

    _setActivatingSurvey(isActivating: boolean) {
        this.activatingSurvey = isActivating;
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
