import React from 'react';
import { HashRouter, Routes, Route } from 'react-router-dom';
import { getSelectedTheme } from './theme/themes';
import { ThemeProvider } from 'styled-components';
import AppNavbar from './app/navbar/AppNavbar';
import AppSidebar from './app/sidebar/AppSidebar';
import NotificationProvider, { useNotification } from './app/notification/NotificationProvider';
import DebugPanel from './app/debug/DebugPanel';
import surveysService from './service/survey/SurveysService';
import pages from './pages/pages';
import WithAuthorization from './components/auth/WithAuthorization';

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
      <ThemeProvider theme={getSelectedTheme()}>
        <NotificationProvider>
          <NotificationHelper />
          <HashRouter>
            <AppNavbar />
            <WithSideBar>
                <Routes>
                  {pages.map((info, index) => {
                    if (info.needAuthorization) {
                      return <Route path={info.path} key={index} element={<WithAuthorization>{info.component}</WithAuthorization>} />;
                    }
                    return <Route path={info.path} key={index} element={info.component} />;
                  })}
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
