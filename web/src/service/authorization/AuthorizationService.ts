import { makeObservable, observable } from 'mobx';
import { instanceOfLoginError, instanceOfLoginSuccessful, Role } from '../../data/exported';
import LocalStorageAdapter, { createLocalStorageAdapter } from '../../adapter/LocalStorageAdapter';
import backendApi from '../../api/backendApiServiceSingleton';
import axios from 'axios';

interface LoginStatus {
}

class LoginLoading implements LoginStatus {}
class LoginSuccess implements LoginStatus {}
class LoginError implements LoginStatus {
    message: string;
    constructor(message: string) {
        this.message = message;
    }
}


export type {
    LoginStatus,
};

export {
    LoginLoading,
    LoginError,
    LoginSuccess,
};

class AuthorizationService {

    authorized: boolean;
    readonly roles: Role[];

    private localStorage: LocalStorageAdapter;

    constructor() {
        console.log('Authorization service');
        this.authorized = false;
        this.roles = [];

        this.localStorage = createLocalStorageAdapter("authorization");

        makeObservable(
            this,
            {
                authorized: observable,
                roles: observable,
            },
        );

        this.installInterceptors();

        const savedToken = this.localStorage.get("token");
        if (savedToken !== null) {
            this.authorized = true;
        }
    }

    login(login: string, password: string): Promise<LoginStatus> {
        return backendApi.login(login, password)
            .then(data => {
                if (instanceOfLoginSuccessful(data)) {
                    this.storeToken(data.token);
                    return new LoginSuccess();
                }
                if (instanceOfLoginError(data)) {
                    return new LoginError(data.error);
                }
                return new LoginError("Unknown error");
            })
            .catch(error => {
                return new LoginError(error.message);
            })
    }

    private storeToken(token: string) {
        this.localStorage.set("token", token);
        this.authorized = true;
    }

    private installInterceptors() {
        const getToken = this.getToken.bind(this);
        axios.interceptors.request.use(function (config) {
            const headers = config.headers;
            const currentToken = getToken();
            if (headers && currentToken !== null) {
                headers['Authorization'] = 'Bearer ' + currentToken;
                config.headers = headers;
            }
            return config;
        })
    }

    private checkAuthPinging() {
        if (this.authorized) {
        }
    }

    private getToken(): string | null {
        return this.localStorage.get("token")
    }
}

const authorizationService = new AuthorizationService();

export default authorizationService;

export type {
    AuthorizationService,
}
