import React from 'react';
import DemoPage from './pages/demo/DemoPage';
import { HashRouter, Routes, Route } from 'react-router-dom';
import { defaultTheme } from './theme/themes';
import { ThemeProvider } from 'styled-components';
import HomePage from './pages/home/HomePage';
import AppNavbar from './app/navbar/AppNavbar';
import AppSidebar from './app/sidebar/AppSidebar';
import CreateSurveyPage from './pages/create_survey/CreateSurveyPage';

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
}

function Root() {
  return (
    <div>
      <ThemeProvider theme={defaultTheme}>
        <HashRouter>
          <AppNavbar />
          <WithSideBar>
              <Routes>
                <Route path="create_survey" element={<CreateSurveyPage/>} />
                <Route path="/demo" element={<DemoPage />} />
                <Route path="/" element={<HomePage/>} />
              </Routes>
          </WithSideBar>
        </HashRouter>
      </ThemeProvider>
    </div>
  );
}

export default Root;
