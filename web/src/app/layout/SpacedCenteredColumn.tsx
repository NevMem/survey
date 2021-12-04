import styled from "styled-components";

const SpacedCenteredColumn = styled.div<{rowGap: number}>`
    display: flex;
    flex-direction: column;
    row-gap: ${props => props.rowGap}px;
    align-items: center;
`;

export default SpacedCenteredColumn;
