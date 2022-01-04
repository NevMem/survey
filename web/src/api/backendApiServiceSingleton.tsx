import MockBackendApiService from "./MockBackendApiService";
import BackendApiServiceImpl from "./BackendApiServiceImpl";
import { BackendApiService } from "./BackendApiService";
import { isFeatureEnabled } from "../service/experiments/utils";
import { apiMockServiceEnabled, useLocalBackend } from "../service/experiments/experiments";

var service: BackendApiService
if (isFeatureEnabled(apiMockServiceEnabled)) {
    service = new MockBackendApiService();
} else {
    var backendUrl = "http://ethnosurvey.com"
    if (isFeatureEnabled(useLocalBackend)) {
        backendUrl = "http://localhost:8080"
    }
    service = new BackendApiServiceImpl(backendUrl);
}

export default service;
