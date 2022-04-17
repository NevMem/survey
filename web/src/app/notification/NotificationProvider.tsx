import { createContext, Dispatch, useContext, useEffect, useReducer, useState } from "react";
import styled from "styled-components";
import Notification from "../../components/notification/Notification";
import { NotificationAction } from "./data";
import { v4 } from 'uuid';

const ADD_NOTIFICATION_ACTION = 'ADD_NOTIFICATION';
const REMOVE_NOTIFICATION_ACTION = 'REMOVE_NOTIFICATION';

interface NotificationData {
    id: string;
    title: string;
    text: string;
    type?: string;
    actions?: NotificationAction[];
};

interface NotificationsStateAction {
    type: string;
    data: NotificationData;
};

const NotificationsTray = styled.div`
    position: fixed;
    right: 0px;
    bottom: 0px;
    display: flex;
    flex-direction: column;
    z-index: 1000;
`;

const NotificationContext = createContext<Dispatch<NotificationsStateAction>>(() => {});

const TimedNotificationWrapper = (props: {dispatcher: Dispatch<NotificationsStateAction>, data: NotificationData}) => {
    const [intervalId, setIntervalId] = useState<any>(0);
    
    const stopTimer = () => {
        clearTimeout(intervalId);
        props.dispatcher({
            type: REMOVE_NOTIFICATION_ACTION,
            data: props.data,
        });
    }

    const startTimer = () => {
        const id = setTimeout(() => {
            stopTimer();
        }, 5000);
        setIntervalId(id);
    };

    useEffect(() => {
        startTimer();
    }, []);
    
    const onAction = (action: string) => {
        props.data.actions?.find(propsAction => propsAction.message === action)?.action(props.data.id)
    }

    return (
        <Notification
            title={props.data.title}
            text={props.data.text}
            type={props.data.type}
            actions={props.data.actions?.map(action => action.message)}
            onAction={onAction}
            />
    );
};

const NotificationProvider = (props: { children: any }) => {
    const [state, dispatcher] = useReducer((state: NotificationData[], action: NotificationsStateAction) => {
        if (action.type === ADD_NOTIFICATION_ACTION) {
            return [...state, action.data];
        }
        if (action.type === REMOVE_NOTIFICATION_ACTION) {
            return state.filter(data => data.id !== action.data.id);
        }
        return state;
    }, []);

    return (
        <NotificationContext.Provider value={dispatcher}>
            <NotificationsTray>
                {state.map(data => {
                    return (
                        <TimedNotificationWrapper key={data.id} dispatcher={dispatcher} data={data} />
                    );
                })}
            </NotificationsTray>
            {props.children}
        </NotificationContext.Provider>
    );
};

export default NotificationProvider;

export const useNotification = () => {
    const dispatcher = useContext(NotificationContext);
    
    return (title: string, text: string, type?: string, actions?: NotificationAction[]): string => {
        const id = v4();
        dispatcher({
            type: ADD_NOTIFICATION_ACTION,
            data: {
                id: id,
                title: title,
                text: text,
                type: type,
                actions: actions,
            }
        });
        return id;
    };
};
