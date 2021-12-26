import { CommonQuestion } from "./exported";
import { Question } from "./exported";


interface SurveyMetadata {
    answers: number;
    files: number;
};

interface UnsavedSurvey {
    name: string;
    questions: Question[];
    commonQuestions: CommonQuestion[];
};

export type {
    SurveyMetadata,
    UnsavedSurvey,
};
