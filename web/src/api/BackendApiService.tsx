import { UnsavedSurvey } from "../data/Survey";
import {
    AllRolesResponse,
    CreateExportDataTaskRequest,
    CreateInviteRequest,
    CreateInviteResponse,
    LoadTaskRequest,
    LoginResponse,
    ManagedUsersResponse,
    RegisterResponse,
    Survey,
    SurveyMetadata,
    Task,
    UpdateRolesRequest,
    Administrator,
    Project,
    ProjectInfo,
    IncomingInvitesResponse,
    OutgoingInvitesResponse,
} from "../data/exported";

interface BackendApiService {
    projects(abortController: AbortController): Promise<Project[]>

    projectInfo(abortController: AbortController, projectId: number): Promise<ProjectInfo>

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
    fetchMetadata(surveyId: number): Promise<SurveyMetadata>

    checkAuth(token: string): Promise<void>
    login(login: string, password: string): Promise<LoginResponse>
    register(
        name: string,
        surname: string,
        login: string,
        password: string,
        email: string,
        abortController: AbortController,
    ): Promise<RegisterResponse>
    me(): Promise<Administrator>

    incomingInvites(abortController: AbortController): Promise<IncomingInvitesResponse>
    outgoingInvites(abortController: AbortController): Promise<OutgoingInvitesResponse>
    createInvite(request: CreateInviteRequest, abortController: AbortController): Promise<CreateInviteResponse>

    roles(abortController: AbortController): Promise<AllRolesResponse>
    updateRoles(request: UpdateRolesRequest, abortController: AbortController): Promise<void>

    createExportDataTask(request: CreateExportDataTaskRequest, abortController: AbortController): Promise<Task>
    loadTask(request: LoadTaskRequest, abortController: AbortController): Promise<Task>
}

export type {
    BackendApiService
};
