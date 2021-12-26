import Sidebar from "../../components/sidebar/Sidebar";
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import pages from '../../pages/pages';

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
            {pages.filter(info => info.useInSidebar).map((info, index) => {
                return <Link key={index} to={info.path}><AppSidebarItem>{info.name}</AppSidebarItem></Link>
            })}
        </Sidebar>
    );
};

export default AppSidebar;
