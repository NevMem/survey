import HomePage from './home/HomePage';
import SurveysPage from './surveys/SurveysPage';
import CreateSurveyPage from './create_survey/CreateSurveyPage';
import DemoPage from './demo/DemoPage';
import DownloadPage from './download/DownloadPage';
import PushPage from './push/PushPage';
import AuthorizationPage from './auth/AuthorizationPage';
import ProfilePage from './profile/ProfilePage';
import { Role } from '../data/exported';
import InvitePage from './invite/InvitePage';

interface PageInfo {
    name: string;
    component: any;
    path: string;
    useInSidebar: boolean;
    needAuthorization: boolean;
    needRoles?: Role[];
}

const pages: PageInfo[] = [
    {
        name: 'Профиль',
        component: <ProfilePage />,
        path: '/profile',
        useInSidebar: true,
        needAuthorization: true,
    },
    {
        name: 'Домашняя',
        component: <HomePage />,
        path : '/',
        useInSidebar: false,
        needAuthorization: true,
    },
    {
        name: 'Опросы',
        component: <SurveysPage />,
        path : '/surveys',
        useInSidebar: true,
        needAuthorization: true,
        needRoles: [
            {id: 'survey.observer'},
        ],
    },
    {
        name: 'Создать опрос',
        component: <CreateSurveyPage />,
        path : '/create_survey',
        useInSidebar: true,
        needAuthorization: true,
        needRoles: [
            {id: 'survey.creator'},
        ],
    },
    {
        name: "Выгрузка данных",
        component: <DownloadPage />,
        path: "/download",
        useInSidebar: true,
        needAuthorization: true,
        needRoles: [
            {id: 'survey.manager'},
        ],
    },
    {
        name: "Пуши",
        component: <PushPage />,
        path: "/push",
        useInSidebar: true,
        needAuthorization: true,
        needRoles: [
            {id: 'push.manager'},
        ],
    },
    {
        name: "Инвайты",
        component: <InvitePage />,
        path: '/invites',
        useInSidebar: true,
        needAuthorization: true,
        needRoles: [
            {id: 'invite.manager'},
        ],
    },
    {
        name: '[dev] Demo page',
        component: <DemoPage />,
        path: '/demo',
        useInSidebar: true,
        needAuthorization: true,
    },
    {
        name: 'Authorization page',
        component: <AuthorizationPage />,
        path: '/auth',
        useInSidebar: false,
        needAuthorization: false,
    },
]

export default pages;

export type {
    PageInfo,
}
