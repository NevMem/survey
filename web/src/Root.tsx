import React from 'react';
import DemoPage from './pages/demo/DemoPage';
import { HashRouter, Routes, Route } from 'react-router-dom';
import { defaultTheme } from './theme/themes';
import { ThemeProvider } from 'styled-components';
import HomePage from './pages/home/HomePage';
import AppNavbar from './app/navbar/AppNavbar';
import AppSidebar from './app/sidebar/AppSidebar';
import CreateSurveyPage from './pages/create_survey/CreateSurveyPage';
import SurveysPage from './pages/surveys/SurveysPage';
import DownloadPage from './pages/download/DownloadPage';
import NotificationProvider, { useNotification } from './app/notification/NotificationProvider';
import DebugPanel from './app/debug/DebugPanel';
import surveysService from './service/survey/SurveysService';

const WithSideBar = (props: { children: any }) => {
  return (
    <div style={{display: 'flex', flexDirection: 'row'}}>
      <div style={{width: '30vw'}}>
        <AppSidebar />
      </div>

      <div style={{width: '70vw'}}>
        {props.children}
      </div>
    </div>
  );
};

const NotificationHelper = () => {
  const notificationUser = useNotification();
  surveysService.setNotificationUser(notificationUser);
  return null;
}

function Root() {
  return (
    <div>
      <ThemeProvider theme={defaultTheme}>
        <NotificationProvider>
          <NotificationHelper />
          <HashRouter>
            <AppNavbar />
            <WithSideBar>
                <Routes>
                  <Route path="/create_survey" element={<CreateSurveyPage/>} />
                  <Route path="/surveys" element={<SurveysPage />} />
                  <Route path="/download" element={<DownloadPage />} />
                  <Route path="/demo" element={<DemoPage />} />
                  <Route path="/" element={<HomePage/>} />
                </Routes>
            </WithSideBar>
          </HashRouter>
          <DebugPanel />
        </NotificationProvider>
      </ThemeProvider>
    </div>
  );
}

export default Root;
