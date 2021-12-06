import { RatingQuestion, StarsQuestion, TextQuestion } from '../data/Question';
import { Survey, SurveyMetadata, UnsavedSurvey } from '../data/Survey';
import { networkFailuresFeature } from '../service/experiments/experiments';
import { isFeatureEnabled } from '../service/experiments/utils';
import { BackendApiService } from './BackendApiService';

class MockBackendApiService implements BackendApiService {
    surveys: Survey[]

    constructor() {
        this.surveys = [
            {
                id: 1,
                questions: [
                    {
                        title: 'На сколько вы оцениваете свою длмашку?',
                        min: 0,
                        max: 10
                    } as RatingQuestion,
                    {
                        title: 'Оцените еще что-нибудь',
                        stars: 5
                    } as StarsQuestion,
                    {
                        title: 'Напишите что-нибудь',
                        maxLength: 500
                    } as TextQuestion
                ],
                name: 'Первый замоканный опрос',
                commonQuestions: [],
                active: false,
            },
            {
                id: 2,
                questions: [
                    {
                        title: '[второй] На сколько вы оцениваете свою длмашку?',
                        min: 0,
                        max: 5
                    } as RatingQuestion,
                    {
                        title: '[второй] Оцените еще что-нибудь',
                        stars: 10
                    } as StarsQuestion,
                    {
                        title: '[второй] Напишите что-нибудь',
                        maxLength: 500
                    } as TextQuestion
                ],
                name: 'Второй замоканный опрос',
                commonQuestions: [],
                active: true,
            }
        ]
    }

    fetchSurveys(): Promise<Survey[]> {
        if (isFeatureEnabled(networkFailuresFeature)) {
            return new Promise((_, rej) => setTimeout(rej.bind(rej, 'Network failed'), 1000));
        }

        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                return this.surveys;
            });
    }

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey> {
        if (isFeatureEnabled(networkFailuresFeature)) {
            return new Promise((_, rej) => setTimeout(rej.bind(rej, 'Network failed'), 1000));
        }

        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                const survey: Survey = {
                    id: 2 + this.surveys.length,
                    name: unsavedSurvey.name,
                    questions: unsavedSurvey.questions,
                    commonQuestions: unsavedSurvey.commonQuestions,
                    active: false
                };
                this.surveys.push(survey);
                return survey;
            });
    }

    activateSurvey(id: number): Promise<void> {
        if (isFeatureEnabled(networkFailuresFeature)) {
            return new Promise((_, rej) => setTimeout(rej.bind(rej, 'Network failed'), 1000));
        }

        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                this.surveys.forEach(survey => {
                    survey.active = survey.id === id;
                });
            });
    }


    fetchMetadata(surveyId: number): Promise<SurveyMetadata> {
        if (isFeatureEnabled(networkFailuresFeature)) {
            return new Promise((_, rej) => setTimeout(rej.bind(rej, 'Network failed'), 1000));
        }

        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                return {
                    files: 56,
                    answers: 48,
                };
            });
    }
};

export default MockBackendApiService;
