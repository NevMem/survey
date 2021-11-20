import styled from "styled-components";

const SecondaryButton = styled.button`
    color: ${props => props.theme.secondary};
    font-size: 1em;
    padding: 8px 18px;
    background-color: ${props => props.theme.foreground};
    border: 2px solid ${props => props.theme.secondary};
    border-radius: 4px;
`;

export default SecondaryButton;
