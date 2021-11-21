import Sidebar from "../../components/sidebar/Sidebar";
import styled from 'styled-components';
import { Link } from 'react-router-dom';

const AppSidebarItem = styled.p`
    font-size: 1.2em;
    margin-top: 10px;
    margin-bottom: 10px;
    color: ${props => props.theme.primary};
    cursor: pointer;
    text-decoration: none;
`;

const AppSidebar = () => {
    return (
        <Sidebar>
            <Link to='/'><AppSidebarItem>На главную</AppSidebarItem></Link>
            <Link to='/surveys'><AppSidebarItem>Опросы</AppSidebarItem></Link>
            <Link to='/create_survey'><AppSidebarItem>Создать опрос</AppSidebarItem></Link>
            <Link to='/push'><AppSidebarItem>Пуши</AppSidebarItem></Link>
            <Link to='/demo'><AppSidebarItem>[demo] компоненты</AppSidebarItem></Link>
        </Sidebar>
    );
};

export default AppSidebar;
