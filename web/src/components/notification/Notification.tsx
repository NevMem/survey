import { useContext } from "react";
import styled, { keyframes, ThemeContext } from "styled-components";
import SpaceBetweenReversedRow from "../../app/layout/SpaceBetweenReversedRow";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import Text from '../text/Text';

const appearAnimation = keyframes`
    0% { transform: translateX(100%); }
    100% { transform: translateX(0%); }
`;

const NotificationWrapper = styled.div<{color: string}>`
    width: 250px;
    padding: 12px;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    row-gap: 16px;
    background-color: ${props => props.color};
    margin: 8px;
    animation: 0.25s ease-out 0s 1 ${appearAnimation};
`;

const NotificationButton = styled.div<{color: string}>`
    padding: 8px;
    border-radius: 8px;
    background-color: ${props => props.color};
    cursor: pointer;
    padding-left: 12px;
    padding-right: 12px;
`;

const ActionsBlock = (props: {buttonsColor: string, actions?: string[], onAction?: (action: string) => void}) => {
    if (!props.actions) {
        return null;
    }

    return (
        <SpaceBetweenReversedRow>
            {props.actions.map(action => {
                return <NotificationButton color={props.buttonsColor} onClick={() => props.onAction?.(action)}>{action}</NotificationButton>;
            })}
        </SpaceBetweenReversedRow>
    )
};

const Notification = (props: {title: string, text: string, type?: string, actions?: string[], onAction?: (action: string) => void}) => {
    const backgroundTheme = useContext(ThemeContext).withAlpha(150);
    const buttonsTheme = useContext(ThemeContext).withAlpha(200);
    var color = backgroundTheme.secondaryBackground;
    var buttonsColor = buttonsTheme.secondaryBackground;
    if (props.type) {
        switch (props.type) {
            case 'success':
                color = backgroundTheme.success;
                buttonsColor = buttonsTheme.success;
                break;
            case 'error':
                color = backgroundTheme.error;
                buttonsColor = buttonsTheme.error;
                break;
            case 'warning':
                color = backgroundTheme.warning;
                buttonsColor = buttonsTheme.warning;
        }
    }

    return (
        <NotificationWrapper color={color}>
            <SpaceBetweenRow>
                <Text large>{props.title}</Text>
                <Text large>&times;</Text>
            </SpaceBetweenRow>
            <Text>{props.text}</Text>
            <ActionsBlock actions={props.actions} onAction={props.onAction} buttonsColor={buttonsColor} />
        </NotificationWrapper>
    );
};

export default Notification;
