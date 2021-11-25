import { Survey, UnsavedSurvey } from "../data/Survey";

interface BackendApiService {
    fetchSurveys(): Promise<Survey[]>
    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
}

export type {
    BackendApiService
};
