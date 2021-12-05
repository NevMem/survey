import { CommonQuestion } from "./CommonQuestion";
import { Question } from "./Question";

interface Survey {
    id: number;
    name: string;
    questions: Question[];
    commonQuestions: CommonQuestion[];
    active: boolean;
};

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
    Survey,
    SurveyMetadata,
    UnsavedSurvey,
};
