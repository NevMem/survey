import React from 'react';
import DemoPage from './pages/demo/DemoPage';
import { HashRouter, Routes, Route } from 'react-router-dom';
import { defaultTheme } from './theme/themes';
import { ThemeProvider } from 'styled-components';
import HomePage from './pages/home/HomePage';
import AppNavbar from './app/navbar/AppNavbar';
import AppSidebar from './app/sidebar/AppSidebar';

const WithSideBar = (props: { children: any }) => {
  return (
    <div style={{display: 'flex', flexDirection: 'row'}}>
      <div style={{width: '30vw'}}>
        <AppSidebar />
      </div>

      <div style={{width: '70vh'}}>
        {props.children}
      </div>
    </div>
  );
}

function Root() {
  return (
    <div>
      <ThemeProvider theme={defaultTheme}>
        <AppNavbar />
        <WithSideBar>
          <HashRouter>
            <Routes>
              <Route path="/demo" element={<DemoPage />} />
              <Route path="/" element={<HomePage/>} />
            </Routes>
          </HashRouter>
        </WithSideBar>
      </ThemeProvider>
    </div>
  );
}

export default Root;
