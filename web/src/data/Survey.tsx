import { Question } from "./Question";

interface Survey {
    id: number
    name: string
    questions: Question[]
    active: boolean
};

interface UnsavedSurvey {
    name: string
    questions: Question[]
};

export type {
    Survey,
    UnsavedSurvey,
};
