import styled from 'styled-components';

const GeneralNavbar = styled.div`
    background-color: ${props => props.theme.primary};
    color: ${props => props.theme.foreground};
    padding: 20px 20px;
    display: flex;
    flex-direction: row;
    font-size: 2em;
`;

export default GeneralNavbar;
