import { makeObservable, observable, action } from 'mobx';
import { UnsavedSurvey } from '../../data/Survey';
import apiService from '../../api/backendApiServiceSingleton';
import { NotificationAction } from '../../app/notification/data';

class SurveysService {
    addingSurvey: boolean;
    activatingSurvey: boolean;
    notificationUser?: (title: string, text: string, type?: string, actions?: NotificationAction[]) => string;

    constructor() {
        this.addingSurvey = false;
        this.activatingSurvey = false;

        makeObservable(
            this,
            {
                addingSurvey: observable,
                activatingSurvey: observable,
                addSurvey: action,
                _setAddingSurvey: action,
                _setActivatingSurvey: action,
            }
        );
    }
    
    setNotificationUser(notificationUser: (title: string, text: string, type?: string, actions?: NotificationAction[]) => string) {
        this.notificationUser = notificationUser;
    }

    addSurvey(unsavedSurvey: UnsavedSurvey) {
        this.addingSurvey = true;
        apiService.addSurvey(unsavedSurvey)
            .then(survey => {
                this.notificationUser?.("Опрос добавился", "", 'success');
                this._setAddingSurvey(false);
            })
            .catch(error => {
                this._setAddingSurvey(false);
                this.notificationUser?.('Ошибка добавлениия опроса', error + "", 'error');
            });
    }

    _setAddingSurvey(isAdding: boolean) {
        this.addingSurvey = isAdding;
    }

    _setActivatingSurvey(isActivating: boolean) {
        this.activatingSurvey = isActivating;
    }
};

const surveysService = new SurveysService();

export default surveysService;

export type {
    SurveysService,
};
