import { UnsavedSurvey, SurveyMetadata } from "../data/Survey";
import { CreateInviteResponse, CreateInviteRequest, GetInvitesResponse, LoginResponse, RegisterResponse, Survey, User, ManagedUsersResponse, AllRolesResponse, UpdateRolesRequest } from "../data/exported";

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
    me(): Promise<User>

    invites(abortController: AbortController): Promise<GetInvitesResponse>
    createInvite(request: CreateInviteRequest, abortController: AbortController): Promise<CreateInviteResponse>
    managedUsers(abortController: AbortController): Promise<ManagedUsersResponse>

    roles(abortController: AbortController): Promise<AllRolesResponse>
    updateRoles(request: UpdateRolesRequest, abortController: AbortController): Promise<void>
}

export type {
    BackendApiService
};
