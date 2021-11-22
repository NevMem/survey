import { makeObservable, observable, action } from "mobx";
import Question from "../../data/Question";

class CreateSurveyService {
    name: string = ""
    questions: Question[] = [];

    constructor() {
        makeObservable(
            this,
            {
                questions: observable,
                name: observable,
                reset: action,
                setName: action,
            }
        )

        const maybeName = localStorage.getItem('create-survey-service.name')
        if (maybeName) {
            this.setName(maybeName)
        }
    }

    setName(name: string) {
        this.name = name
        localStorage.setItem('create-survey-service.name', name)
    }
    
    addQuestion(question: Question) {
        this.questions.push(question)
    }

    reset() {
        this.questions = [];
    }
};

const createSurveyService = new CreateSurveyService()

export default createSurveyService;
export type {
    CreateSurveyService,
}
