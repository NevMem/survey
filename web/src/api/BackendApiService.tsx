import { UnsavedSurvey, SurveyMetadata } from "../data/Survey";
import { LoginResponse, RegisterResponse, Survey } from "../data/exported";

interface BackendApiService {
    fetchSurveys(): Promise<Survey[]>
    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
    fetchMetadata(surveyId: number): Promise<SurveyMetadata>

    checkAuth(token: string): Promise<void>
    login(login: string, password: string): Promise<LoginResponse>
    register(name: string, surname: string, login: string, password: string, email: string, inviteId: string): Promise<RegisterResponse>
}

export type {
    BackendApiService
};
