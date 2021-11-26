import { RatingQuestion, StarsQuestion, TextQuestion } from '../data/Question';
import { Survey, UnsavedSurvey } from '../data/Survey';
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
                active: true,
            }
        ]
    }

    fetchSurveys(): Promise<Survey[]> {
        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                return this.surveys;
            });
    }

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey> {
        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                const survey: Survey = {
                    id: 2 + this.surveys.length,
                    name: unsavedSurvey.name,
                    questions: unsavedSurvey.questions,
                    active: false
                };
                this.surveys.push(survey);
                return survey;
            });
    }

    activateSurvey(id: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, 1000))
            .then(() => {
                this.surveys.forEach(survey => {
                    survey.active = survey.id === id;
                });
            });
    }
};

export default MockBackendApiService;
