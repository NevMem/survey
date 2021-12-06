import { CommonQuestion } from "./CommonQuestion"

const ageCommonQuestion: CommonQuestion = {
    id: 'age',
};

const gradeCommonQuestion: CommonQuestion = {
    id: 'grade',
};

const schoolNameCommonQuestion: CommonQuestion = {
    id: 'school_name',
};

const regionCommonQuestion: CommonQuestion = {
    id: 'region',
};

const commonQuestions = [
    ageCommonQuestion,
    schoolNameCommonQuestion,
    regionCommonQuestion,
    gradeCommonQuestion,
];


const commonQuestionTitle = (commonQuestion: CommonQuestion): string => {
    switch (commonQuestion) {
        case ageCommonQuestion:
            return "Возраст";
        case schoolNameCommonQuestion:
            return "Название школы";
        case regionCommonQuestion:
            return "Регион";
        case gradeCommonQuestion:
            return "Номер класса";
    }
    return commonQuestion.id;
} 

export {
    commonQuestions,
    commonQuestionTitle,
};
