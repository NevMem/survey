import { Question } from "./Question";

interface Survey {
    id: number
    name: string
    questions: Question[]
    active: boolean
};

interface SurveyMetadata {
    answers: number;
    files: number;
};

interface UnsavedSurvey {
    name: string
    questions: Question[]
};

export type {
    Survey,
    SurveyMetadata,
    UnsavedSurvey,
};
