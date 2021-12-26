import HomePage from './home/HomePage';
import SurveysPage from './surveys/SurveysPage';
import CreateSurveyPage from './create_survey/CreateSurveyPage';
import DemoPage from './demo/DemoPage';
import DownloadPage from './download/DownloadPage';
import PushPage from './push/PushPage';

interface PageInfo {
    name: string;
    component: any;
    path: string;
    useInSidebar: boolean;
}

const pages: PageInfo[] = [
    {
        name: 'Домашняя',
        component: <HomePage />,
        path : '/',
        useInSidebar: false,
    },
    {
        name: 'Опросы',
        component: <SurveysPage />,
        path : '/surveys',
        useInSidebar: true,
    },
    {
        name: 'Создать опрос',
        component: <CreateSurveyPage />,
        path : '/create_survey',
        useInSidebar: true,
    },
    {
        name: "Выгрузка данных",
        component: <DownloadPage />,
        path: "/download",
        useInSidebar: true,
    },
    {
        name: "Пуши",
        component: <PushPage />,
        path: "/push",
        useInSidebar: true,
    },
    {
        name: '[dev] Demo page',
        component: <DemoPage />,
        path: '/demo',
        useInSidebar: true,
    },
]

export default pages;

export type {
    PageInfo,
}
