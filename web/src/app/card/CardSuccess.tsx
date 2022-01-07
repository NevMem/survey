import styled from 'styled-components';

const CardSuccess = styled.div`
    padding: 16px;
    border-radius: 8px;
    background-color: ${props => props.theme.withAlpha(30).success};
`;

export default CardSuccess;
