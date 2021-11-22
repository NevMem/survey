import PrimaryButton from './PrimaryButton';
import SecondaryButton from './SecondaryButton';
import React from 'react';

const GeneralButton = (props: {secondary?: boolean, children: string, onClick?: React.MouseEventHandler<HTMLButtonElement>}) => {
    if (props.secondary) {
        return <SecondaryButton onClick={props.onClick}>{props.children}</SecondaryButton>
    }
    return <PrimaryButton onClick={props.onClick}>{props.children}</PrimaryButton>    
};

export default GeneralButton;
