import styled from 'styled-components';

const OutlinedCard = styled.div`
    padding: 16px;
    border-radius: 8px;
    background-color: ${props => props.theme.background};
    border: 2px solid ${props => props.theme.secondaryBackground};
`;

export default OutlinedCard;
