import { Survey, UnsavedSurvey } from "../data/Survey";

interface BackendApiService {
    fetchSurveys(): Promise<Survey[]>
    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
    activateSurvey(id: number): Promise<void>
}

export type {
    BackendApiService
};
