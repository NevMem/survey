import styled from "styled-components";

const SecondaryButton = styled.button`
    color: ${props => props.theme.secondary};
    font-size: 1em;
    padding: 8px 18px;
    background-color: rgba(0, 0, 0, 0);
    border: 2px solid ${props => props.theme.secondary};
    border-radius: 4px;
    transition: all ease-in 0.2s;
    cursor: pointer;

    &:hover {
        color: ${props => props.theme.foreground};
        background-color: ${props => props.theme.secondary};
        border: 2px solid ${props => props.theme.secondary};
    }
`;

const SecondaryDisabledButton = styled.button`
    color: ${props => props.theme.withAlpha(40).secondary};
    font-size: 1em;
    padding: 8px 18px;
    background-color: rgba(0, 0, 0, 0);
    border: 2px solid ${props => props.theme.withAlpha(40).secondary};
    border-radius: 4px;
    transition: all ease-in 0.2s;
    cursor: pointer;
`;

export { 
    SecondaryButton,
    SecondaryDisabledButton,
};
