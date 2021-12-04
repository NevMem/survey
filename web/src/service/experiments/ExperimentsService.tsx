import LocalStorageAdapter, { createLocalStorageAdapter } from "../../adapter/LocalStorageAdapter";
import { Feature } from "./data";

class ExperimentsService {
    data: LocalStorageAdapter;
    
    constructor() {
        this.data = createLocalStorageAdapter("experiments-service");
    }

    isFeatureEnabled(feature: Feature): boolean {
        return this.data.get(`feature-${feature.name}`) == "enabled";
    }

    setFeatureEnabled(feature: Feature, isEnabled: boolean) {
        this.data.set(`feature-${feature.name}`, isEnabled ? "enabled" : "disabled");
    }
};

const experimentsService = new ExperimentsService();

export default experimentsService;

export type {
    ExperimentsService,
};
