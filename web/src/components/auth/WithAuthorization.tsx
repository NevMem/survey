import { observer } from 'mobx-react-lite';
import authService, { AuthorizationService } from '../../service/authorization/AuthorizationService';
import CardError from '../../app/card/CardError';


const WithAuthorization = observer((props: {authService: AuthorizationService, children: any}) => {
    console.log('WithAuthorization');
    console.log(props.authService.authorized);
    if (props.authService.authorized) {
        return props.children;
    }
    return (
        <CardError>

        </CardError>
    )
})

const WithAuthorizationWrapper = (props: {children: any}) => {
    return <WithAuthorization authService={authService}>{props.children}</WithAuthorization>
};

export default WithAuthorizationWrapper;
