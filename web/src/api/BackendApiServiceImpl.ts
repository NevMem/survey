import { Survey, LoginResponse, RegisterResponse } from "../data/exported";
import { UnsavedSurvey, SurveyMetadata } from "../data/Survey";
import { BackendApiService } from "./BackendApiService";
import axios, { AxiosResponse } from 'axios';


class BackendApiServiceImpl implements BackendApiService {

    private baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    fetchSurveys(): Promise<Survey[]> {
        this.get('/v1/surveys')
            .then(data => console.log(data))
            .catch(err => {
                // console.log(err.toJSON())
                console.log(err);
            })
        return new Promise((res, rej) => rej());
    }
    addSurvey(unsavedSurvey: UnsavedSurvey): Promise<Survey> {
        throw new Error("Method not implemented.");
    }
    fetchMetadata(surveyId: number): Promise<SurveyMetadata> {
        throw new Error("Method not implemented.");
    }
    checkAuth(token: string): Promise<void> {
        throw new Error("Method not implemented.");
    }
    login(login: string, password: string): Promise<LoginResponse> {
        throw new Error("Method not implemented.");
    }
    register(name: string, surname: string, login: string, password: string, email: string, inviteId: string): Promise<RegisterResponse> {
        throw new Error("Method not implemented.");
    }
    
    private get<T>(path: string): Promise<AxiosResponse<T>> {
        return axios.get<T>(this.baseUrl + path);
    }
};

export default BackendApiServiceImpl;
