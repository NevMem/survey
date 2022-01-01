interface SizesTheme {
    smallTextSize: string;
    mediumTextSize: string;
    largeTextSize: string;
    headerTextSize: string;
}

interface PaletteTheme {
    primary: string;
    secondary: string;
    foreground: string;
    background: string;
    secondaryBackground: string;
    success: string;
    error: string;
    warning: string;
    grey: string;
};

export interface Theme extends PaletteTheme, SizesTheme {
    name: string;
    withAlpha(alpha: number): PaletteTheme;
};

export type {
    PaletteTheme,
    SizesTheme,
};
