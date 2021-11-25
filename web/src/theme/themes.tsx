import { Theme } from "./theme";

const createDefaultTheme = (): Theme => {
    return {
        primary: 'tomato',
        secondary: '#a03020',
        foreground: 'white',
        background: 'white',
        secondaryBackgrond: '#f5f5f5',
        smallTextSize: '0.7em',
        mediumTextSize: '1em',
        largeTextSize: '1.3em',
        headerTextSize: '2em',
    }
}

const createDebugTheme = (): Theme => {
    return {
        primary: 'green',
        secondary: 'red',
        foreground: 'blue',
        background: 'white',
        secondaryBackgrond: '#e0e0e0',
        smallTextSize: '0.7em',
        mediumTextSize: '1em',
        largeTextSize: '1.3em',
        headerTextSize: '2em',
    }
}

const defaultTheme = createDefaultTheme();

const themes: Theme[] = [
    defaultTheme,
    createDebugTheme(),
]

export default themes;
export {
    defaultTheme
};
