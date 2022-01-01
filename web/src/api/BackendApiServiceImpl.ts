import { Survey, LoginResponse, RegisterResponse, LoginRequest, AllSurveysResponse, RegisterRequest } from "../data/exported";
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
        throw new Error("Method not implemented.");
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

    private post<T, U>(path: string, body: U): Promise<AxiosResponse<T>> {
        return axios.post<T>(this.baseUrl + path, body);
    }
    
    private get<T>(path: string): Promise<AxiosResponse<T>> {
        return axios.get<T>(this.baseUrl + path);
    }
};

export default BackendApiServiceImpl;
