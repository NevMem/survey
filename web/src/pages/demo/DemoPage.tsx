import { ThemeProvider } from 'styled-components';
import GeneralButton from '../../components/button/GeneralButton';
import Loader from '../../components/loader/Loader';
import themes from '../../theme/themes';
import Text from '../../components/text/Text';
import SpaceBetweenRow from '../../app/layout/SpaceBetweenRow';
import Badge from '../../components/badge/Badge';
import Notification from '../../components/notification/Notification';
import { useNotification } from '../../app/notification/NotificationProvider';

const NotificationsBlock = () => {
    const notificationUser = useNotification();
    const notification = (type: string) => {
        notificationUser('Notification', 'Notification body', type);
    };

    const notificationWithAction = (type: string) => {
        notificationUser(
            'Notification with action',
            'Notification body',
            type,
            [
                {
                    message: 'Ok',
                    action: (notificationId: string) => {
                        notificationUser('Got action for notification with id', notificationId);
                    }
                }
            ]
        );
    }

    return (
        <SpaceBetweenRow>
            <Text large>Нотификации:</Text>
            <GeneralButton onClick={() => notification('success')}>Success</GeneralButton>
            <GeneralButton onClick={() => notification('error')}>Error</GeneralButton>
            <GeneralButton onClick={() => notification('warning')}>Warning</GeneralButton>
            <GeneralButton onClick={() => notification('default')}>Default</GeneralButton>
            <GeneralButton onClick={() => notificationWithAction('success')}>Success w action</GeneralButton>
            <GeneralButton onClick={() => notificationWithAction('error')}>Error w action</GeneralButton>
            <GeneralButton onClick={() => notificationWithAction('warning')}>Warning w action</GeneralButton>
            <GeneralButton onClick={() => notificationWithAction('default')}>Default w action</GeneralButton>
        </SpaceBetweenRow>
    );
};

export default function DemoPage() {
    return (
        <div>
            {themes.map((theme, index) => {
                return (
                    <section key={index}>
                        <ThemeProvider theme={theme}>
                            <GeneralButton>Primary</GeneralButton>
                            <GeneralButton secondary>Secondary</GeneralButton>

                            <Text header>Header</Text>
                            <Text large>Large</Text>
                            <Text>Medium</Text>
                            <Text small>Small</Text>

                            <Loader small />
                            <Loader />
                            <Loader large />

                            <SpaceBetweenRow>
                                <Loader large />
                                <Text large>Large</Text>
                                <GeneralButton secondary>Button</GeneralButton>
                            </SpaceBetweenRow>

                            <SpaceBetweenRow>
                                <Badge error>Error</Badge>
                                <Badge warning>Warning</Badge>
                                <Badge>Default</Badge>
                                <Badge success>Success</Badge>
                            </SpaceBetweenRow>

                            <Notification title='Нотификация' text='Просто какое-то уведомление' />
                            <Notification title='Нотификация' text='Просто какое-то уведомление' type='success' />
                            <Notification title='Нотификация' text='Просто какое-то уведомление' type='error' />
                            <Notification title='Нотификация' text='Просто какое-то уведомление' type='warning' />
                        </ThemeProvider>
                    </section>
                )
            })}

            <NotificationsBlock />
        </div>
    )
};
