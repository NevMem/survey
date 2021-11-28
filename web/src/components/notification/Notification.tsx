import { useContext } from "react";
import styled, { ThemeContext } from "styled-components";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import Text from '../text/Text';

const NotificationWrapper = styled.div<{color: string}>`
    width: 200px;
    padding: 12px;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    row-gap: 16px;
    background-color: ${props => props.color};
    margin: 8px;
`;

const Notification = (props: {title: string, text: string, type?: string}) => {
    const theme = useContext(ThemeContext).withAlpha(150);
    var color = theme.secondaryBackground;
    if (props.type) {
        switch (props.type) {
            case 'success':
                color = theme.success;
                break;
            case 'error':
                color = theme.error;
                break;
            case 'warning':
                color = theme.warning;
        }
    }

    return (
        <NotificationWrapper color={color}>
            <SpaceBetweenRow>
                <Text large>{props.title}</Text>
                <Text large>&times;</Text>
            </SpaceBetweenRow>
            <Text>{props.text}</Text>
        </NotificationWrapper>
    );
};

export default Notification;