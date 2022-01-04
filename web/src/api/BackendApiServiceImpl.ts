import { Survey, LoginResponse, RegisterResponse, LoginRequest, AllSurveysResponse, RegisterRequest, User, CreateSurveyRequest, CreateSurveyResponse, instanceOfCreateSurveySuccess, instanceOfCreateSurveyError, GetInvitesResponse } from "../data/exported";
import { UnsavedSurvey, SurveyMetadata } from "../data/Survey";
import { BackendApiService } from "./BackendApiService";
import axios, { AxiosResponse } from 'axios';


class BackendApiServiceImpl implements BackendApiService {

    private baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    fetchSurveys(): Promise<Survey[]> {
        return this.get<AllSurveysResponse>('/v1/surveys')
            .then(data => data.data.surveys);
    }

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey> {
        const request: CreateSurveyRequest = {
            name: unsavedSurvey.name,
            questions: unsavedSurvey.questions,
            commonQuestions: unsavedSurvey.commonQuestions,
        };

        return this.post<CreateSurveyResponse, CreateSurveyRequest>('/v1/create_survey', request)
            .then(data => data.data)
            .then(response => {
                if (instanceOfCreateSurveySuccess(response)) {
                    return response.survey;
                }
                if (instanceOfCreateSurveyError(response)) {
                    throw new Error(response.message);
                }
                throw new Error("Unknown error");
            });
    }

    fetchMetadata(surveyId: number): Promise<SurveyMetadata> {
        throw new Error("Method not implemented.");
    }

    checkAuth(token: string): Promise<void> {
        return this.get<void>('/v1/check_auth')
            .then(data => data.data);
    }

    login(login: string, password: string): Promise<LoginResponse> {
        const request: LoginRequest = {
            login: login,
            password: password,
        };
        return this.post<LoginResponse, LoginRequest>('/v1/login', request)
            .then(data => data.data)
    }

    register(name: string, surname: string, login: string, password: string, email: string, inviteId: string): Promise<RegisterResponse> {
        const request: RegisterRequest = {
            name: name,
            surname: surname,
            login: login,
            password: password,
            email: email,
            inviteId: inviteId,
        };

        return this.post<RegisterResponse, RegisterRequest>('/v1/register', request)
            .then(data => data.data);
    }

    me(): Promise<User> {
        return this.get<User>('/v1/me')
            .then(data => data.data);
    }

    invites(): Promise<GetInvitesResponse> {
        return this.get<GetInvitesResponse>('/v1/my_invites').then(data => data.data);
    }

    private post<T, U>(path: string, body: U): Promise<AxiosResponse<T>> {
        return axios.post<T>(this.baseUrl + path, body);
    }
    
    private get<T>(path: string): Promise<AxiosResponse<T>> {
        return axios.get<T>(this.baseUrl + path);
    }
};

export default BackendApiServiceImpl;
