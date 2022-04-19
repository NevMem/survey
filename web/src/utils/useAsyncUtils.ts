import { useState, useEffect } from 'react';

class RequestState<T> {};
class RequestProcessing<T> implements RequestState<T> {};
class RequestError<T> implements RequestState<T> {
    message: string;
    constructor(message: string) {
        this.message = message;
    }
};
class RequestSuccess<T> implements RequestState<T> {
    result: T;
    constructor(result: T) {
        this.result = result;
    }
};

function useAsyncRequest<T>(requestBuilder: (abortController: AbortController) => Promise<T>): RequestState<T> {
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
    RequestState,
    RequestProcessing,
    RequestError,
    RequestSuccess,
};
