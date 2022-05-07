import { CommonQuestion } from "./exported";
import { Question } from "./exported";


interface UnsavedSurvey {
    projectId: number;
    name: string;
    questions: Question[];
    commonQuestions: CommonQuestion[];
    answerCoolDown: number;
};

export type {
    UnsavedSurvey,
};
