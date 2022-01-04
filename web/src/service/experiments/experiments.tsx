import { Feature } from "./data";

const networkFailuresFeature: Feature = { name: 'network-fails' };
const apiMockServiceEnabled: Feature = { name: 'mock-api-service' };
const messUpAuthorizationHeader: Feature = { name: 'mess-up-authorization' };
const doNotClearSurveyAfterCreation: Feature = { name: 'do-not-clear-survey-after-creation' };
const useLocalBackend: Feature = { name: 'use-local-backend' };

const features: Feature[] = [
    networkFailuresFeature,
    apiMockServiceEnabled,
    messUpAuthorizationHeader,
    doNotClearSurveyAfterCreation,
    useLocalBackend,
];

export {
    networkFailuresFeature,
    apiMockServiceEnabled,
    messUpAuthorizationHeader,
    doNotClearSurveyAfterCreation,
    useLocalBackend,

    features,
};
