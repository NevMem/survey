import { Survey, UnsavedSurvey, SurveyMetadata } from "../data/Survey";

interface BackendApiService {
    fetchSurveys(): Promise<Survey[]>
    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
    activateSurvey(id: number): Promise<void>
    fetchMetadata(surveyId: number): Promise<SurveyMetadata>
}

export type {
    BackendApiService
};
