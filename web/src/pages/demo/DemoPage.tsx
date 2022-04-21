import { ThemeProvider } from 'styled-components';
import GeneralButton from '../../components/button/GeneralButton';
import Loader from '../../components/loader/Loader';
import themes from '../../theme/themes';
import Text from '../../components/text/Text';
import SpaceBetweenRow from '../../app/layout/SpaceBetweenRow';
import Badge from '../../components/badge/Badge';
import { useNotification } from '../../app/notification/NotificationProvider';
import ThemePicker from '../../components/theme/ThemePicker';
import Card from '../../app/card/Card';
import CardError from '../../app/card/CardError';
import CardSuccess from '../../app/card/CardSuccess';
import SpacedColumn from '../../app/layout/SpacedColumn';
import { Task, TaskState } from '../../data/exported';
import TaskView from '../../app/task/TaskView';
import NotificationsBlock from './NotificationsBlock';

const NotificationsButtonsBlock = () => {
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

const TasksBlock = () => {
    const readyTask: Task = {
        id: 33,
        projectId: 0,
        state: TaskState.Success,
        log: [
            { message: 'started', timestamp: (new Date()).getSeconds() },
            { message: 'running 1', timestamp: (new Date()).getSeconds() },
            { message: 'running 2', timestamp: (new Date()).getSeconds() },
            { message: 'ended', timestamp: (new Date()).getSeconds() },
        ],
        outputs: [
            { id: 0, filename: 'somefile.png', url: '', bucketName: '' },
            { id: 1, filename: 'somefile2.png', url: '', bucketName: '' },
            { id: 2, filename: 'somefile3.png', url: '', bucketName: '' },
            { id: 3, filename: 'somefile4.png', url: '', bucketName: '' },
        ],
    };

    const executingTask: Task = {
        id: 33,
        projectId: 0,
        state: TaskState.Executing,
        log: [
            { message: 'started', timestamp: (new Date()).getSeconds() },
            { message: 'running 1', timestamp: (new Date()).getSeconds() },
            { message: 'running 2', timestamp: (new Date()).getSeconds() },
        ],
        outputs: [
            { id: 0, filename: 'somefile.png', url: '', bucketName: '' },
            { id: 1, filename: 'somefile2.png', url: '', bucketName: '' },
        ],
    };

    const errorTask: Task = {
        id: 33,
        projectId: 0,
        state: TaskState.Error,
        log: [
            { message: 'started', timestamp: (new Date()).getSeconds() },
            { message: 'running 1', timestamp: (new Date()).getSeconds() },
            { message: 'running 2', timestamp: (new Date()).getSeconds() },
            { message: 'Exception: exception', timestamp: (new Date()).getSeconds() },
        ],
        outputs: [
        ],
    };

    return (
        <SpacedColumn rowGap={16}>
            <TaskView task={readyTask} expandedDefault={true} />
            <TaskView task={executingTask} />
            <TaskView task={errorTask} />
        </SpacedColumn>
    )
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

                            <GeneralButton disabled>Primary disabled</GeneralButton>
                            <GeneralButton secondary disabled>Secondary disabled</GeneralButton>

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
                                <Badge info>Info</Badge>
                            </SpaceBetweenRow>

                            <NotificationsBlock />

                            <SpacedColumn rowGap={16}>
                                <Card>
                                    <Text>usual card</Text>
                                </Card>

                                <CardError>
                                    <Text>error card</Text>
                                </CardError>

                                <CardSuccess>
                                    <Text>success card</Text>
                                </CardSuccess>
                            </SpacedColumn>

                            <ThemePicker />

                            <TasksBlock />
                        </ThemeProvider>
                    </section>
                )
            })}

            <NotificationsButtonsBlock />
        </div>
    )
};
