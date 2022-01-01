import styled from 'styled-components';

const GeneralNavbar = styled.div`
    background-color: ${props => props.theme.primary};
    color: ${props => props.theme.foreground};
    padding: 20px 20px;
    font-size: 2em;
    column-gap: 10px;
`;

export default GeneralNavbar;
