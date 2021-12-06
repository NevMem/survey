import styled from "styled-components";

const Input = styled.input`
    outline: none;
    padding: 4px;
    padding-left: 10px;
    border: 2px solid ${props => props.theme.secondaryBackground};
    border-radius: 4px;
    background-color: ${props => props.theme.background};
    font-size: 1.5em;
`;

export default Input;
