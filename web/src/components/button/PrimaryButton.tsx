import styled from "styled-components";

const PrimaryButton = styled.button`
    color: ${props => props.theme.foreground};
    font-size: 1em;
    padding: 8px 18px;
    background-color: ${props => props.theme.primary};
    border: 2px solid ${props => props.theme.primary};
    border-radius: 4px;
    cursor: pointer;
    transition: all ease-in 0.2s;

    &:hover {
        background-color: rgba(0, 0, 0, 0);
        color: ${props => props.theme.primary};
    }
`;

const PrimaryDisabledButton = styled.button`
    color: ${props => props.theme.foreground};
    font-size: 1em;
    padding: 8px 18px;
    background-color: ${props => props.theme.withAlpha(40).primary};
    border: 2px solid ${props => props.theme.withAlpha(40).primary};
    border-radius: 4px;
    cursor: pointer;
    transition: all ease-in 0.2s;
`;

export {
    PrimaryButton,
    PrimaryDisabledButton,
};
