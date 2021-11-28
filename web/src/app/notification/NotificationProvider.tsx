import { createContext, Dispatch, useContext, useEffect, useReducer, useState } from "react";
import styled from "styled-components";
import Notification from "../../components/notification/Notification";
import { v4 } from 'uuid';

const ADD_NOTIFICATION_ACTION = 'ADD_NOTIFICATION';
const REMOVE_NOTIFICATION_ACTION = 'REMOVE_NOTIFICATION';

interface NotificationData {
    id: string;
    title: string;
    text: string;
    type?: string;
};

interface NotificationAction {
    type: string;
    data: NotificationData;
};

const NotificationsTray = styled.div`
    position: fixed;
    right: 0px;
    bottom: 0px;
    display: flex;
    flex-direction: column;
`;

const NotificationContext = createContext<Dispatch<NotificationAction>>(() => {});

const TimedNotificationWrapper = (props: {dispatcher: Dispatch<NotificationAction>, data: NotificationData}) => {
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

    return (
        <Notification title={props.data.title} text={props.data.text} type={props.data.type} />
    );
};

const NotificationProvider = (props: { children: any }) => {
    const [state, dispatcher] = useReducer((state: NotificationData[], action: NotificationAction) => {
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
    
    return (title: string, text: string, type?: string) => {
        dispatcher({
            type: ADD_NOTIFICATION_ACTION,
            data: {
                id: v4(),
                title: title,
                text: text,
                type: type,
            }
        });
    };
};
