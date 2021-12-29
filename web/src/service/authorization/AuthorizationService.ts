import { makeObservable, observable } from 'mobx';
import { Role } from '../../data/exported';
import LocalStorageAdapter, { createLocalStorageAdapter } from '../../adapter/LocalStorageAdapter';
import backendApi from '../../api/backendApiServiceSingleton';
import axios from 'axios';

class AuthorizationService {

    readonly authorized: boolean;
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

    private installInterceptors() {
        const getToken = this.getToken.bind(this);
        axios.interceptors.request.use(function (config) {
            const headers = config.headers;
            if (headers) {
                headers['Authorization'] = 'Bearer ' + getToken();
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
