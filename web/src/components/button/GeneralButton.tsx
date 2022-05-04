import { PrimaryButton, PrimaryDisabledButton, } from './PrimaryButton';
import { SecondaryButton, SecondaryDisabledButton, } from './SecondaryButton';
import React from 'react';

const GeneralButton = (props: {secondary?: boolean, disabled?: boolean, children: string, onClick?: React.MouseEventHandler<HTMLButtonElement>}) => {
    if (props.disabled) {
        if (props.secondary) {
            return <SecondaryDisabledButton onClick={props.onClick} disabled={true}>{props.children}</SecondaryDisabledButton>
        }
        return <PrimaryDisabledButton onClick={props.onClick} disabled={true}>{props.children}</PrimaryDisabledButton>
    }
    if (props.secondary) {
        return <SecondaryButton onClick={props.onClick}>{props.children}</SecondaryButton>
    }
    return <PrimaryButton onClick={props.onClick}>{props.children}</PrimaryButton>    
};

export default GeneralButton;
