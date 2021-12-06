import { Feature } from "./data"
import experimentsService from "./ExperimentsService"

const isFeatureEnabled = (feature: Feature) => {
    return experimentsService.isFeatureEnabled(feature);
};

export {
    isFeatureEnabled,
};
