import { useNavigate } from "react-router-dom";

class Navigator {
    private navigate: (path: string) => void;

    constructor(navigate: (path: string) => void) {
        this.navigate = navigate;
    }

    home() {
        this.navigate('/');
    }

    surveyInfoPage(surveyId: number) {
        this.navigate(`/survey/${surveyId}`);
    }
}

const useNavigator = () => {
    return new Navigator(useNavigate());
};

export default useNavigator;

export {
    Navigator,
};
