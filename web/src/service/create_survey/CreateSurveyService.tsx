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
    }

    setName(name: string) {
        this.name = name
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
