import styled from "styled-components";

const SpacedCenteredRow = styled.div<{columnGap: number}>`
    display: flex;
    flex-direction: row;
    column-gap: ${props => props.columnGap}px;
    align-items: center;
`;

export default SpacedCenteredRow;
