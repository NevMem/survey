import styled from "styled-components";

const PrimaryButton = styled.button`
    color: ${props => props.theme.foreground};
    font-size: 1em;
    padding: 10px 20px;
    background-color: ${props => props.theme.primary};
    border: none;
    border-radius: 4px;
`;

export default PrimaryButton;
