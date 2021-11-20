import PrimaryButton from './PrimaryButton';
import SecondaryButton from './SecondaryButton';

const GeneralButton = (props: {secondary: boolean, children: string}) => {
    if (props.secondary) {
        return <SecondaryButton>{props.children}</SecondaryButton>
    }
    return <PrimaryButton>{props.children}</PrimaryButton>    
};

export default GeneralButton;
