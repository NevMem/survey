import { UnsavedSurvey, SurveyMetadata } from "../data/Survey";
import { CreateInviteResponse, CreateInviteRequest, GetInvitesResponse, LoginResponse, RegisterResponse, Survey, User } from "../data/exported";

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
}

export type {
    BackendApiService
};
