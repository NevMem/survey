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
    CreateProjectRequest,
    Invite,
    AcceptInviteResponse,
    AcceptInviteRequest,
    MediaGallery,
    TasksInSurvey,
} from "../data/exported";

interface BackendApiService {
    projects(abortController: AbortController): Promise<Project[]>

    project(abortController: AbortController, id: number): Promise<Project>

    projectInfo(abortController: AbortController, projectId: number): Promise<ProjectInfo>
    surveys(abortController: AbortController, projectId: number): Promise<Survey[]>
    survey(abortController: AbortController, id: number): Promise<Survey>

    createProject(abortController: AbortController, request: CreateProjectRequest): Promise<Project>

    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
    fetchMetadata(abortController: AbortController, surveyId: number): Promise<SurveyMetadata>

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
    accept(abortController: AbortController, request: AcceptInviteRequest): Promise<AcceptInviteResponse>

    roles(abortController: AbortController): Promise<AllRolesResponse>
    updateRoles(request: UpdateRolesRequest, abortController: AbortController): Promise<void>

    createExportDataTask(request: CreateExportDataTaskRequest, abortController: AbortController): Promise<Task>
    loadTask(request: LoadTaskRequest, abortController: AbortController): Promise<Task>
    tasksInSurvey(request: TasksInSurvey, abortController: AbortController): Promise<Task[]>

    gallery(abortController: AbortController, id: number): Promise<MediaGallery>
}

export type {
    BackendApiService
};
