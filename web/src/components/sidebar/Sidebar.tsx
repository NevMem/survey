import styled from "styled-components";

const Sidebar = styled.div`
    padding: 20px 20px;
    border-radius: 16px;
    background-color: ${props => props.theme.secondaryBackground};
    margin: 20px;
    display: flex;
    flex-direction: column;
`;

export default Sidebar;
