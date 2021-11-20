import { Theme } from "./theme";

const createDefaultTheme = (): Theme => {
    return {
        primary: 'tomato',
        secondary: '#a03020',
        foreground: 'white',
    }
}

const createDebugTheme = (): Theme => {
    return {
        primary: 'green',
        secondary: 'red',
        foreground: 'blue',
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
