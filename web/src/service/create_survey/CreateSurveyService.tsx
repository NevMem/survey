import { makeObservable, observable, action } from "mobx";
import { Question } from "../../data/exported";
import { createLocalStorageAdapter } from '../../adapter/LocalStorageAdapter';

class CreateSurveyService {
    name: string = ""
    questions: Question[] = [];
    localStorageAdapter = createLocalStorageAdapter('create-survey-service.ver1')

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

        const maybeName = this.localStorageAdapter.get('name');
        if (maybeName) {
            this.setName(maybeName);
        }

        const maybeQuestions = this.localStorageAdapter.get('questions');
        if (maybeQuestions) {
            const newQuestions: Question[] = JSON.parse(maybeQuestions);
            newQuestions.forEach(question => {
                this.addQuestion(question);
            });
        }
    }

    setName(name: string) {
        this.name = name
        this.localStorageAdapter.set('name', name);
    }
    
    addQuestion(question: Question) {
        this.questions.push(question);
        this.localStorageAdapter.set('questions', JSON.stringify(this.questions));
    }

    reset() {
        this.setName('');
        this.questions = [];
        this.localStorageAdapter.delete('questions');
    }
};

const createSurveyService = new CreateSurveyService()

export default createSurveyService;
export type {
    CreateSurveyService,
}
