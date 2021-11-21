import Sidebar from "../../components/sidebar/Sidebar";
import styled from 'styled-components';

const AppSidebarItem = styled.p`
    font-size: 1.2em;
    margin-top: 10px;
    margin-bottom: 10px;
    color: ${props => props.theme.primary};
    cursor: pointer;
`;

const AppSidebar = () => {
    return (
        <Sidebar>
            <AppSidebarItem>На главную</AppSidebarItem>
            <AppSidebarItem>Опросы</AppSidebarItem>
            <AppSidebarItem>Создать опрос</AppSidebarItem>
        </Sidebar>
    );
};

export default AppSidebar;
