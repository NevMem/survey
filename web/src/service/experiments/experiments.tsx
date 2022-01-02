import { Feature } from "./data";

const networkFailuresFeature: Feature = { name: 'network-fails' };
const apiMockServiceEnabled: Feature = { name: 'mock-api-service' };
const messUpAuthorizationHeader: Feature = { name: 'mess-up-authorization' };

const features: Feature[] = [
    networkFailuresFeature,
    apiMockServiceEnabled,
    messUpAuthorizationHeader,
];

export {
    networkFailuresFeature,
    apiMockServiceEnabled,
    messUpAuthorizationHeader,

    features,
};
