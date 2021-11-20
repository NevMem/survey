import React from 'react';
import DemoPage from './pages/demo/DemoPage';
import { HashRouter, Routes, Route } from 'react-router-dom';

function Root() {
  return (
    <div>
      <HashRouter>
        <Routes>
          <Route path="/demo" element={<DemoPage />} />
        </Routes>
      </HashRouter>
    </div>
  );
}

export default Root;
