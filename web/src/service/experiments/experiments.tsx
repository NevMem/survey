import { Feature } from "./data";

const networkFailuresFeature: Feature = { name: 'network-fails' };
const apiMockServiceEnabled: Feature = { name: 'mock-api-service' };

const features: Feature[] = [
    networkFailuresFeature,
    apiMockServiceEnabled,
];

export {
    networkFailuresFeature,
    apiMockServiceEnabled,

    features,
};
