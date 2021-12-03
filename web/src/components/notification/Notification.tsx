import { useContext } from "react";
import styled, { keyframes, ThemeContext } from "styled-components";
import SpaceBetweenReversedRow from "../../app/layout/SpaceBetweenReversedRow";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import GeneralButton from "../button/GeneralButton";
import Text from '../text/Text';

const appearAnimation = keyframes`
    0% { transform: translateX(100%); }
    100% { transform: translateX(0%); }
`;

const NotificationWrapper = styled.div<{color: string}>`
    width: 200px;
    padding: 12px;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    row-gap: 16px;
    background-color: ${props => props.color};
    margin: 8px;
    animation: 1s ease-out 0s 1 ${appearAnimation};
`;

const ActionsBlock = (props: {actions?: string[], onAction?: (action: string) => void}) => {
    if (!props.actions) {
        return null;
    }

    return (
        <SpaceBetweenReversedRow>
            {props.actions.map(action => {
                return <GeneralButton secondary onClick={() => props.onAction?.(action)}>{action}</GeneralButton>;
            })}
        </SpaceBetweenReversedRow>
    )
};

const Notification = (props: {title: string, text: string, type?: string, actions?: string[], onAction?: (action: string) => void}) => {
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
            <ActionsBlock actions={props.actions} onAction={props.onAction} />
        </NotificationWrapper>
    );
};

export default Notification;
