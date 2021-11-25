import React from 'react';
import { render, screen } from '@testing-library/react';
import Root from './Root';

test('renders header', () => {
  render(<Root />);
  const headerText = screen.getByText(/Survey/i);
  expect(headerText).toBeInTheDocument();
});

test('renders navbar', () => {
  render(<Root />);
  const navText = screen.getByText(/Опросы/i);
  expect(navText).toBeInTheDocument();
})
