import { UnsavedSurvey, SurveyMetadata } from "../data/Survey";
import { Survey } from "../data/exported";

interface BackendApiService {
    fetchSurveys(): Promise<Survey[]>
    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey>
    fetchMetadata(surveyId: number): Promise<SurveyMetadata>
}

export type {
    BackendApiService
};
