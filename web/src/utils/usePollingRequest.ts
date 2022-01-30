import { useState, useEffect } from 'react';

interface PollingState {};
class PollingProcessing implements PollingState {};
class PollingError implements PollingState {
    message: string;
    constructor(message: string) {
        this.message = message;
    }
};
class PollingSuccess<T> implements PollingState {
    result: T;
    constructor(result: T) {
        this.result = result;
    }
};

function usePollingRequest<T>(
    requestBuilder: (abortController: AbortController) => Promise<T>,
    shouldStopHandle: (state: PollingState) => boolean,
): PollingState {
    const [state, setState] = useState(new PollingProcessing());
    const [requestIndex, setRequestIndex] = useState(0);
    const [intervalId, setIntervalId] = useState<NodeJS.Timeout | undefined>(undefined);

    useEffect(() => {
        const abortController = new AbortController();

        const updateStateIfNeeded = (newState: PollingState) => {
            setState(newState);
            if (shouldStopHandle(newState)) {
                if (intervalId) {
                    clearInterval(intervalId);
                    setIntervalId(undefined);
                }
            }
        };

        requestBuilder(abortController)
            .then(data => {
                updateStateIfNeeded(new PollingSuccess<T>(data));
            })
            .catch(error => {
                updateStateIfNeeded(new PollingError(error + ""));
            });
        return () => {
            abortController.abort();
        };
    }, [requestIndex]);

    useEffect(() => {
        const id = setInterval(() => {
            setRequestIndex(requestIndex + 1);
        }, 1000);
        setIntervalId(id);
        return () => {
            if (intervalId) {
                clearInterval(id);
            }
        };
    }, []);

    return state;
};

export default usePollingRequest;
export {
    PollingProcessing,
    PollingError,
    PollingSuccess,
};
export type {
    PollingState,
};
