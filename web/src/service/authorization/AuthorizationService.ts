import { makeObservable, observable } from 'mobx';
import { Role } from '../../data/exported';
import LocalStorageAdapter, { createLocalStorageAdapter } from '../../adapter/LocalStorageAdapter';

class AuthorizationService {

    readonly authorized: boolean;
    readonly roles: Role[];

    private localStorage: LocalStorageAdapter;

    constructor() {
        this.authorized = false;
        this.roles = [];

        this.localStorage = createLocalStorageAdapter("authorization");

        makeObservable(
            this,
            {
                authorized: observable,
                roles: observable,
            },
        )
    }
}

const authorizationService = new AuthorizationService();

export default authorizationService;

export type {
    AuthorizationService,
}
