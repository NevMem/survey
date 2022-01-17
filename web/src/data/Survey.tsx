import { CommonQuestion } from "./exported";
import { Question } from "./exported";


interface UnsavedSurvey {
    name: string;
    questions: Question[];
    commonQuestions: CommonQuestion[];
};

export type {
    UnsavedSurvey,
};
