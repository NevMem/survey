import { createLocalStorageAdapter } from "../adapter/LocalStorageAdapter";
import { Theme, PaletteTheme, SizesTheme } from "./theme";

const applyAlpha = (color: string, alpha: number) => {
    function hexToRgb(hex: string) {
        var result = /^#?([A-Fa-f\d]{2})([A-Fa-f\d]{2})([A-Fa-f\d]{2})$/i.exec(hex);
        return result ? {
          r: parseInt(result[1], 16),
          g: parseInt(result[2], 16),
          b: parseInt(result[3], 16)
        } : null;
    }
    const rgb = hexToRgb(color);
    if (!rgb) {
        throw new Error(`Messed up color: ${color}`);
    }
    const colorStr = `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, ${alpha * 1.0 / 255.0})`;
    return colorStr;
};

class AlphaTheme implements PaletteTheme {
    primary: string;
    secondary: string;
    foreground: string;
    background: string;
    secondaryBackground: string;
    success: string;
    error: string;
    warning: string;
    grey: string;

    constructor(palette: PaletteTheme, alpha: number) {
        this.primary = applyAlpha(palette.primary, alpha);
        this.secondary = applyAlpha(palette.secondary, alpha);
        this.foreground = applyAlpha(palette.foreground, alpha);
        this.background = applyAlpha(palette.background, alpha);
        this.secondaryBackground = applyAlpha(palette.secondaryBackground, alpha);
        this.success = applyAlpha(palette.success, alpha);
        this.error = applyAlpha(palette.error, alpha);
        this.warning = applyAlpha(palette.warning, alpha);
        this.grey = applyAlpha(palette.grey, alpha);
    }
};

class ThemeImpl implements Theme {
    name: string;
    palette: PaletteTheme;
    primary: string;
    secondary: string;
    foreground: string;
    background: string;
    secondaryBackground: string;
    smallTextSize: string;
    mediumTextSize: string;
    largeTextSize: string;
    headerTextSize: string;
    success: string;
    error: string;
    warning: string;
    grey: string;

    constructor(name: string, palette: PaletteTheme, sizes: SizesTheme) {
        this.name = name;

        this.palette = palette;
        
        this.primary = palette.primary;
        this.secondary = palette.secondary;
        this.foreground = palette.foreground;
        this.background = palette.background;
        this.secondaryBackground = palette.secondaryBackground;
        this.smallTextSize = sizes.smallTextSize;
        this.mediumTextSize = sizes.mediumTextSize;
        this.largeTextSize = sizes.largeTextSize;
        this.headerTextSize = sizes.headerTextSize;
        this.success = palette.success;
        this.error = palette.error;
        this.warning = palette.warning;
        this.grey = palette.grey;

        this.withAlpha = this.withAlpha.bind(this);
    }

    withAlpha(alpha: number): PaletteTheme {
        return new AlphaTheme(this.palette, alpha); // optimize this
    }
};

const createDefaultTheme = (): Theme => {
    return new ThemeImpl(
        'default',
        {
            primary: '#ff6347',
            secondary: '#a03020',
            foreground: '#FFFFFF',
            background: '#FFFFFF',
            secondaryBackground: '#f5f5f5',
            success: '#61E294',
            warning: '#FFD25A',
            error: '#FF785A',
            grey: '#d0d0d0',
        }, {
            smallTextSize: '0.7em',
            mediumTextSize: '1em',
            largeTextSize: '1.3em',
            headerTextSize: '2em',
        }
    );
}

const createDebugTheme = (): Theme => {
    return new ThemeImpl(
        'debug theme',
        {
            primary: '#00FF00',
            secondary: '#FF0000',
            foreground: '#0000FF',
            background: '#FFFFFF',
            secondaryBackground: '#e0e0e0',
            success: '#61E294',
            warning: '#FFD25A',
            error: '#FF785A',
            grey: '#e0e0e0',
        }, {
            smallTextSize: '0.7em',
            mediumTextSize: '1em',
            largeTextSize: '1.3em',
            headerTextSize: '2em',
        }
    );
}

const defaultTheme = createDefaultTheme();

const themes: Theme[] = [
    defaultTheme,
    createDebugTheme(),
]

const saveTheme = (name: string) => {
    const adapter = createLocalStorageAdapter('theme');
    adapter.set('name', name);
    window.location.reload();
};

const getSelectedTheme = (): Theme => {
    const adapter = createLocalStorageAdapter('theme');
    const name = adapter.get('name');
    const foundTheme = themes.find(theme => theme.name === name);
    if (foundTheme === undefined) {
        return defaultTheme;
    }
    return foundTheme;
};

export default themes;
export {
    getSelectedTheme,
    saveTheme,
};
