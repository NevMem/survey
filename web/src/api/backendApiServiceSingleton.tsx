import MockBackendApiService from "./MockBackendApiService";
import BackendApiServiceImpl from "./BackendApiServiceImpl";
import { BackendApiService } from "./BackendApiService";
import { isFeatureEnabled } from "../service/experiments/utils";
import { apiMockServiceEnabled } from "../service/experiments/experiments";

var service: BackendApiService
if (isFeatureEnabled(apiMockServiceEnabled)) {
    service = new MockBackendApiService();
} else {
    service = new BackendApiServiceImpl("http://localhost:8080");
}

export default service;
