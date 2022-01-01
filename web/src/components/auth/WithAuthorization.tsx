import { observer } from 'mobx-react-lite';
import authService, { AuthorizationService } from '../../service/authorization/AuthorizationService';
import { Navigate } from 'react-router';


const WithAuthorization = observer((props: {authService: AuthorizationService, children: any}) => {
    if (props.authService.authorized) {
        return props.children;
    }
    return (
        <Navigate to="/auth" />
    );
})

const WithAuthorizationWrapper = (props: {children: any}) => {
    return <WithAuthorization authService={authService}>{props.children}</WithAuthorization>
};

export default WithAuthorizationWrapper;
