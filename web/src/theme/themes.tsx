import { Theme } from "./theme"

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

const themes: Theme[] = [
    createDefaultTheme(),
    createDebugTheme(),
]

export default themes
