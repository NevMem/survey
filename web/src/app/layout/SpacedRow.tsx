import styled from "styled-components";

const SpacedRow = styled.div<{columnGap: number}>`
    display: flex;
    flex-direction: row;
    column-gap: ${props => props.columnGap}px;
`;

export default SpacedRow;
