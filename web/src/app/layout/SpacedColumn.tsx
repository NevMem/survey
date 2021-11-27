import styled from "styled-components";

const SpacedColumn = styled.div<{rowGap: number}>`
    display: flex;
    flex-direction: column;
    row-gap: ${props => props.rowGap}px;
`;

export default SpacedColumn;
