import {
    AllRolesResponse,
    AllSurveysResponse,
    CreateExportDataTaskRequest,
    CreateInviteRequest,
    CreateInviteResponse,
    CreateSurveyRequest,
    CreateSurveyResponse,
    GetInvitesResponse,
    instanceOfCreateSurveyError,
    instanceOfCreateSurveySuccess,
    LoadSurveyMetadataRequest,
    LoadSurveyMetadataResponse,
    LoadTaskRequest,
    LoginRequest,
    LoginResponse,
    ManagedUsersResponse,
    RegisterRequest,
    RegisterResponse,
    Survey,
    Task,
    UpdateRolesRequest,
    User,
} from "../data/exported";
import { UnsavedSurvey } from "../data/Survey";
import { BackendApiService } from "./BackendApiService";
import axios, { AxiosResponse } from 'axios';
import { SurveyMetadata } from "../data/exported";

class BackendApiServiceImpl implements BackendApiService {

    private baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    fetchSurveys(): Promise<Survey[]> {
        return this.get<AllSurveysResponse>('/v1/survey/surveys')
            .then(data => data.data.surveys);
    }

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey> {
        const request: CreateSurveyRequest = {
            name: unsavedSurvey.name,
            questions: unsavedSurvey.questions,
            commonQuestions: unsavedSurvey.commonQuestions,
        };

        return this.post<CreateSurveyResponse, CreateSurveyRequest>('/v1/survey/create_survey', request)
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
        let request: LoadSurveyMetadataRequest = {
            surveyId: surveyId,
        };

        return this.post<LoadSurveyMetadataResponse, LoadSurveyMetadataRequest>('/v1/survey/metadata', request)
            .then(data => data.data)
            .then(data => data.surveyMetadata);
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
        return this.get<GetInvitesResponse>('/v1/invite/my_invites').then(data => data.data);
    }

    createInvite(request: CreateInviteRequest, abortController: AbortController): Promise<CreateInviteResponse> {
        return this.post<CreateInviteResponse, CreateInviteRequest>('/v1/invite/create_invite', request, abortController).then(data => data.data);
    }

    managedUsers(abortController: AbortController): Promise<ManagedUsersResponse> {
        return this.get<ManagedUsersResponse>('/v1/role/managed_users', abortController).then(data => data.data);
    }

    roles(abortController: AbortController): Promise<AllRolesResponse> {
        return this.get<AllRolesResponse>('/roles', abortController).then(data => data.data);
    }

    updateRoles(request: UpdateRolesRequest, abortController: AbortController): Promise<void> {
        return this.post<void, UpdateRolesRequest>('/v1/role/update_roles', request, abortController).then(data => data.data);
    }

    createExportDataTask(request: CreateExportDataTaskRequest, abortController: AbortController): Promise<Task> {
        return this.post<Task, CreateExportDataTaskRequest>('/v1/task/create_export_data_task', request, abortController).then(data => data.data);
    }

    loadTask(request: LoadTaskRequest, abortController: AbortController): Promise<Task> {
        return this.post<Task, LoadTaskRequest>('/v1/task/task', request, abortController).then(data => data.data);
    }

    private post<T, U>(path: string, body: U, abortController: AbortController | undefined = undefined): Promise<AxiosResponse<T>> {
        if (abortController) {
            return axios.post<T>(this.baseUrl + path, body, { signal: abortController.signal });
        }
        return axios.post<T>(this.baseUrl + path, body);
    }
    
    private get<T>(path: string, abortController: AbortController | undefined = undefined): Promise<AxiosResponse<T>> {
        if (abortController) {
            return axios.get<T>(this.baseUrl + path, { signal: abortController.signal });
        }
        return axios.get<T>(this.baseUrl + path);
    }
};

export default BackendApiServiceImpl;
