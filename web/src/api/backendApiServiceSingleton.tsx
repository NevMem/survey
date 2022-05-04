import BackendApiServiceImpl from "./BackendApiServiceImpl";
import { BackendApiService } from "./BackendApiService";
import { isFeatureEnabled } from "../service/experiments/utils";
import { useLocalBackend } from "../service/experiments/experiments";

var backendUrl = "https://ethnosurvey.com"
if (isFeatureEnabled(useLocalBackend)) {
    backendUrl = "http://localhost:80"
}

const service: BackendApiService = new BackendApiServiceImpl(backendUrl);

export default service;
