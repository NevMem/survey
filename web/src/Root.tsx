import React from 'react';
import DemoPage from './pages/demo/DemoPage';
import { HashRouter, Routes, Route } from 'react-router-dom';
import { defaultTheme } from './theme/themes';
import { ThemeProvider } from 'styled-components';
import HomePage from './pages/home/HomePage';

function Root() {
  return (
    <div>
      <ThemeProvider theme={defaultTheme}>
        <HashRouter>
          <Routes>
            <Route path="/demo" element={<DemoPage />} />
            <Route path="/" element={<HomePage/>} />
          </Routes>
        </HashRouter>
      </ThemeProvider>
    </div>
  );
}

export default Root;
