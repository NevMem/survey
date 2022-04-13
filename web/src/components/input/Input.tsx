import styled from "styled-components";

const Input = styled.input`
    outline: none;
    padding: 8px;
    padding-left: 16px;
    padding-right: 16px;
    border: 2px solid ${props => props.theme.secondaryBackground};
    border-radius: 4px;
    background-color: ${props => props.theme.background};
    font-size: 1.2em;
`;

export default Input;
