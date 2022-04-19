import {
    AllRolesResponse,
    CreateExportDataTaskRequest,
    CreateInviteRequest,
    CreateInviteResponse,
    CreateSurveyRequest,
    CreateSurveyResponse,
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
    Administrator,
    IncomingInvitesResponse,
    OutgoingInvitesResponse,
    Project,
    ProjectInfo,
    GetProjectsResponse,
    GetProjectInfoRequest,
    GetProjectInfoResponse,
    GetSurveysResponse,
    GetSurveysRequest,
    CreateProjectRequest,
    CreateProjectResponse,
    GetProjectResponse,
    GetProjectRequest,
    AcceptInviteResponse,
    AcceptInviteRequest,
    GetSurveyResponse,
    GetSurveyRequest,
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

    projects(abortController: AbortController): Promise<Project[]> {
        return this.get<GetProjectsResponse>('/v2/projects/all', abortController)
            .then(data => data.data)
            .then(data => data.projects);
    }

    project(abortController: AbortController, id: number): Promise<Project> {
        return this.post<GetProjectResponse, GetProjectRequest>('/v2/projects/get', { id: id }, abortController)
            .then(data => data.data)
            .then(data => data.project);
    }

    surveys(abortController: AbortController, projectId: number): Promise<Survey[]> {
        return this.post<GetSurveysResponse, GetSurveysRequest>('/v2/survey/surveys', { projectId: projectId }, abortController)
            .then(data => data.data)
            .then(data => data.surveys);
    }

    survey(abortController: AbortController, id: number): Promise<Survey> {
        return this.post<GetSurveyResponse, GetSurveyRequest>('/v2/survey/get', { id: id }, abortController)
            .then(data => data.data)
            .then(data => data.survey);
    }

    projectInfo(abortController: AbortController, projectId: number): Promise<ProjectInfo> {
        return this.post<GetProjectInfoResponse, GetProjectInfoRequest>('/v2/projects/info', { projectId: projectId }, abortController)
            .then(data => data.data)
            .then(data => data.projectInfo);
    }

    createProject(abortController: AbortController, request: CreateProjectRequest): Promise<Project> {
        return this.post<CreateProjectResponse, CreateProjectRequest>('/v2/projects/create', request, abortController)
            .then(data => data.data)
            .then(data => data.project);
    }

    incomingInvites(abortController: AbortController): Promise<IncomingInvitesResponse> {
        return this.post<IncomingInvitesResponse, any>('/v2/invites/incoming', {}, abortController)
            .then(data => data.data);
    }

    outgoingInvites(abortController: AbortController): Promise<OutgoingInvitesResponse> {
        return this.post<OutgoingInvitesResponse, any>('/v2/invites/outgoing', {}, abortController)
            .then(data => data.data);
    }

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey> {
        const request: CreateSurveyRequest = {
            projectId: unsavedSurvey.projectId,
            name: unsavedSurvey.name,
            questions: unsavedSurvey.questions,
            commonQuestions: unsavedSurvey.commonQuestions,
            answerCoolDown: -1,
        };

        return this.post<CreateSurveyResponse, CreateSurveyRequest>('/v2/survey/create_survey', request)
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
        return this.get<void>('/v2/check_auth')
            .then(data => data.data);
    }

    login(login: string, password: string): Promise<LoginResponse> {
        const request: LoginRequest = {
            login: login,
            password: password,
        };
        return this.post<LoginResponse, LoginRequest>('/v2/login', request)
            .then(data => data.data)
    }

    register(name: string, surname: string, login: string, password: string, email: string): Promise<RegisterResponse> {
        const request: RegisterRequest = {
            name: name,
            surname: surname,
            login: login,
            password: password,
            email: email,
        };

        return this.post<RegisterResponse, RegisterRequest>('/v2/register', request)
            .then(data => data.data);
    }

    me(): Promise<Administrator> {
        return this.get<Administrator>('/v2/me')
            .then(data => data.data);
    }

    createInvite(request: CreateInviteRequest, abortController: AbortController): Promise<CreateInviteResponse> {
        return this.post<CreateInviteResponse, CreateInviteRequest>('/v2/invites/create', request, abortController).then(data => data.data);
    }

    accept(abortController: AbortController, request: AcceptInviteRequest): Promise<AcceptInviteResponse> {
        return this.post<AcceptInviteResponse, AcceptInviteRequest>('/v2/invites/accept', request, abortController)
            .then(data => data.data);
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
        return this.post<Task, CreateExportDataTaskRequest>('/v2/tasks/create_export_data_task', request, abortController).then(data => data.data);
    }

    loadTask(request: LoadTaskRequest, abortController: AbortController): Promise<Task> {
        return this.post<Task, LoadTaskRequest>('/v2/tasks/get', request, abortController).then(data => data.data);
    }

    private post<T, U>(path: string, body?: U, abortController: AbortController | undefined = undefined): Promise<AxiosResponse<T>> {
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
