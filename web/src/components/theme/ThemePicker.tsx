import styled from 'styled-components';
import themes from '../../theme/themes';

const DropdownContent = styled.div`
    display: none;
    position: absolute;
    z-index: 1;
    cursor: pointer;
    background-color: ${props => props.theme.background};
    padding: 12px;
    left: -12px;
`;

const Dropdown = styled.div`
    position: relative;
    display: inline-block;
    cursor: pointer;

    :hover ${DropdownContent} {
        display: block;
    }
`;

const ThemeBadge = styled.div`
    width: 24px;
    height: 24px;
    border-radius: 12px;
    margin: 2px;
`;

const CurrentThemeBadge = styled.div`
    width: 24px;
    height 24px;
    border-radius: 14px;
    background-color: ${props => props.theme.primary};
    border: 2px solid ${props => props.theme.background};
`;

const ThemePicker = () => {
    return (
        <Dropdown>
            <CurrentThemeBadge />
            <DropdownContent>
                {themes.map((theme, index) => {
                    return <ThemeBadge style={{backgroundColor: theme.primary}} key={index}></ThemeBadge>
                })}
            </DropdownContent>
        </Dropdown>
    );
};

export default ThemePicker;
