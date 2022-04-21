import { Fragment } from "react";
import Row from "../../app/layout/Row";
import Notification from '../../components/notification/Notification';


const NotificationsBlock = () => {
    return (
        <Fragment>
            <Row>
                <Notification title='Нотификация' text='Просто какое-то уведомление' />
                <Notification title='Нотификация с очень большим хедером' text='Просто какое-то уведомление' />
            </Row>
            <Row>
                <Notification title='Нотификация с очень большим хедером и кнопками' text='Просто какое-то уведомление' actions={['ok', 'cancel', 'kek']} />
                <Notification title='Нотификация с очень большим хедером и кнопками' text='Просто какое-то уведомление' actions={['ok', 'cancel', 'kek']} type='success' />
                <Notification title='Нотификация с очень большим хедером и кнопками' text='Просто какое-то уведомление' actions={['ok', 'cancel', 'kek']} type='error' />
                <Notification title='Нотификация с очень большим хедером и кнопками' text='Просто какое-то уведомление' actions={['ok', 'cancel', 'kek']} type='warning' />
            </Row>
            <Row>
                <Notification title='Нотификация' text='Просто какое-то уведомление' type='success' />
                <Notification title='Нотификация' text='Просто какое-то уведомление' type='error' />
                <Notification title='Нотификация' text='Просто какое-то уведомление' type='warning' />
            </Row>
        </Fragment>
    );
};

export default NotificationsBlock;
