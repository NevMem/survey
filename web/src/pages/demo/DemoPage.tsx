import { ThemeProvider } from 'styled-components';
import GeneralButton from '../../components/button/GeneralButton';
import themes from '../../theme/themes';

export default function DemoPage() {
    return (
        <div>
            {themes.map((theme) => {
                return (
                    <section>
                        <ThemeProvider theme={theme}>
                            <GeneralButton secondary={false}>Button</GeneralButton>
                            <GeneralButton secondary={true}>Button</GeneralButton>
                        </ThemeProvider>
                    </section>
                )
            })}
        </div>
    )
}
