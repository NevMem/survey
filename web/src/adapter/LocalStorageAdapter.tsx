
class LocalStorageAdapter {
    _domain: string

    constructor(domain: string) {
        this._domain = domain
    }

    get(key: string): string | null {
        return localStorage.getItem(`${this._domain}.${key}`)
    }

    set(key: string, value: string) {
        localStorage.setItem(`${this._domain}.${key}`, value)
    }

    delete(key: string) {
        localStorage.removeItem(`${this._domain}.${key}`)
    }
};

function createLocalStorageAdapter(domain: string): LocalStorageAdapter {
    return new LocalStorageAdapter(domain);
}

export default LocalStorageAdapter;

export {
    createLocalStorageAdapter
}
