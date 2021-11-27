import Sidebar from "../../components/sidebar/Sidebar";
import styled from 'styled-components';
import { Link } from 'react-router-dom';

const AppSidebarItem = styled.p`
    font-size: 1.2em;
    padding-top: 10px;
    padding-bottom: 10px;
    padding-left: 8px;
    margin: 0px;
    color: ${props => props.theme.primary};
    cursor: pointer;
    text-decoration: none;
    transition: all ease-in 0.1s;
    border-radius: 8px;

    &:hover {
        background-color: ${props => props.theme.primary};
        color: ${props => props.theme.background};
    }
`;

const AppSidebar = () => {
    return (
        <Sidebar>
            <Link to='/'><AppSidebarItem>На главную</AppSidebarItem></Link>
            <Link to='/surveys'><AppSidebarItem>Опросы</AppSidebarItem></Link>
            <Link to='/create_survey'><AppSidebarItem>Создать опрос</AppSidebarItem></Link>
            <Link to='/push'><AppSidebarItem>Пуши</AppSidebarItem></Link>
            <Link to='/download'><AppSidebarItem>Выгрузка данных</AppSidebarItem></Link>
            <Link to='/demo'><AppSidebarItem>[demo] компоненты</AppSidebarItem></Link>
        </Sidebar>
    );
};

export default AppSidebar;
