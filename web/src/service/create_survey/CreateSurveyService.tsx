import { makeObservable, observable, action } from "mobx";
import Question from "../../data/Question";
import { createLocalStorageAdapter } from '../../adapter/LocalStorageAdapter';

class CreateSurveyService {
    name: string = ""
    questions: Question[] = [];
    localStorageAdapter = createLocalStorageAdapter('create-survey-service')

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

        const maybeName = this.localStorageAdapter.get('name')
        if (maybeName) {
            this.setName(maybeName)
        }
    }

    setName(name: string) {
        this.name = name
        this.localStorageAdapter.set('name', name)
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
