import { Feature } from "./data";

const networkFailuresFeature: Feature = { name: 'network-fails' };
const apiMockServiceEnabled: Feature = { name: 'mock-api-service' };
const messUpAuthorizationHeader: Feature = { name: 'mess-up-authorization' };
const doNotClearSurveyAfterCreation: Feature = { name: 'do-not-clear-survey-after-creation' };

const features: Feature[] = [
    networkFailuresFeature,
    apiMockServiceEnabled,
    messUpAuthorizationHeader,
    doNotClearSurveyAfterCreation,
];

export {
    networkFailuresFeature,
    apiMockServiceEnabled,
    messUpAuthorizationHeader,
    doNotClearSurveyAfterCreation,

    features,
};
