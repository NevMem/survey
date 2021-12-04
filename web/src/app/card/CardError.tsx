import styled from 'styled-components';

const CardError = styled.div`
    padding: 16px;
    border-radius: 8px;
    background-color: ${props => props.theme.withAlpha(30).error};
`;

export default CardError;
