import { useState, useEffect } from 'react';

interface RequestState {};
class RequestProcessing implements RequestState {};
class RequestError implements RequestState {
    message: string;
    constructor(message: string) {
        this.message = message;
    }
};
class RequestSuccess<T> implements RequestState {
    result: T;
    constructor(result: T) {
        this.result = result;
    }
};

function useAsyncRequest<T>(requestBuilder: (abortController: AbortController) => Promise<T>): RequestState {
    const [state, setState] = useState(new RequestProcessing());

    useEffect(() => {
        const abortController = new AbortController();
        requestBuilder(abortController)
            .then(data => { setState(new RequestSuccess<T>(data)); })
            .catch(error => { setState(new RequestError(error + "")); });
        return () => {
            abortController.abort();
        };
    }, []);

    return state;
};

export default useAsyncRequest;
export {
    RequestProcessing,
    RequestError,
    RequestSuccess,
};
export type {
    RequestState,
};
