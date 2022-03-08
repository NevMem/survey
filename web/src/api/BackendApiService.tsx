import { UnsavedSurvey } from "../data/Survey";
import {
    AllRolesResponse,
    CreateExportDataTaskRequest,
    CreateInviteRequest,
    CreateInviteResponse,
    GetInvitesResponse,
    LoadTaskRequest,
    LoginResponse,
    ManagedUsersResponse,
    RegisterResponse,
    Survey,
    SurveyMetadata,
    Task,
    UpdateRolesRequest,
    Administrator,
} from "../data/exported";

interface BackendApiService {
    fetchSurveys(): Promise<Survey[]>
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
        inviteId: string,
        abortController: AbortController,
    ): Promise<RegisterResponse>
    me(): Promise<Administrator>

    invites(abortController: AbortController): Promise<GetInvitesResponse>
    createInvite(request: CreateInviteRequest, abortController: AbortController): Promise<CreateInviteResponse>
    managedUsers(abortController: AbortController): Promise<ManagedUsersResponse>

    roles(abortController: AbortController): Promise<AllRolesResponse>
    updateRoles(request: UpdateRolesRequest, abortController: AbortController): Promise<void>

    createExportDataTask(request: CreateExportDataTaskRequest, abortController: AbortController): Promise<Task>
    loadTask(request: LoadTaskRequest, abortController: AbortController): Promise<Task>
}

export type {
    BackendApiService
};
