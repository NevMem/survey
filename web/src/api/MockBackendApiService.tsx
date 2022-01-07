import { AllRolesResponse, CreateInviteResponse, GetInvitesResponse, LoginResponse, ManagedUsersResponse, RatingQuestion, RegisterResponse, StarsQuestion, TextQuestion, User } from '../data/exported';
import { SurveyMetadata, UnsavedSurvey } from '../data/Survey';
import { networkFailuresFeature } from '../service/experiments/experiments';
import { isFeatureEnabled } from '../service/experiments/utils';
import { BackendApiService } from './BackendApiService';
import { Survey } from '../data/exported';

class MockBackendApiService implements BackendApiService {
    surveys: Survey[]

    constructor() {
        this.surveys = [
            {
                id: 1,
                surveyId: "KNXKNK",
                questions: [
                    {
                        type: "rating",
                        title: 'На сколько вы оцениваете свою длмашку?',
                        min: 0,
                        max: 10
                    } as RatingQuestion,
                    {
                        type: "stars",
                        title: 'Оцените еще что-нибудь',
                        stars: 5
                    } as StarsQuestion,
                    {
                        type: "text",
                        title: 'Напишите что-нибудь',
                        maxLength: 500
                    } as TextQuestion
                ],
                name: 'Первый замоканный опрос',
                commonQuestions: [],
            },
            {
                id: 2,
                surveyId: "DKAMD",
                questions: [
                    {
                        type: "rating",
                        title: '[второй] На сколько вы оцениваете свою длмашку?',
                        min: 0,
                        max: 5
                    } as RatingQuestion,
                    {
                        type: "stars",
                        title: '[второй] Оцените еще что-нибудь',
                        stars: 10
                    } as StarsQuestion,
                    {
                        type: "text",
                        title: '[второй] Напишите что-нибудь',
                        maxLength: 500
                    } as TextQuestion
                ],
                name: 'Второй замоканный опрос',
                commonQuestions: [],
            }
        ]
    }

    checkAuth(token: string): Promise<void> {
        throw new Error('Method not implemented.');
    }

    login(login: string, password: string): Promise<LoginResponse> {
        throw new Error('Method not implemented.');
    }

    register(name: string, surname: string, login: string, password: string, email: string, inviteId: string): Promise<RegisterResponse> {
        throw new Error('Method not implemented.');
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
                    surveyId: 2 + this.surveys.length + "",
                    name: unsavedSurvey.name,
                    questions: unsavedSurvey.questions,
                    commonQuestions: unsavedSurvey.commonQuestions
                };
                this.surveys.push(survey);
                return survey;
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

    me(): Promise<User> {
        throw new Error('Method not implemented.');
    }

    invites(abortController: AbortController): Promise<GetInvitesResponse> {
        throw new Error('Method not implemented.');
    }

    createInvite(): Promise<CreateInviteResponse> {
        throw new Error('Method not implemented.');
    }

    managedUsers(): Promise<ManagedUsersResponse> {
        throw new Error('Method not implemented.');
    }

    roles(): Promise<AllRolesResponse> {
        throw new Error('Method not implemented.');
    }

    updateRoles(): Promise<void> {
        throw new Error('Method not implemented.');
    }
};

export default MockBackendApiService;
