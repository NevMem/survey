import { ThemeProvider } from 'styled-components';
import GeneralButton from '../../components/button/GeneralButton';
import Text, { TextStyle } from '../../components/text/Text';
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

                            <Text style={TextStyle.Large}>Large</Text>
                            <Text style={TextStyle.Medium}>Medium</Text>
                            <Text style={TextStyle.Small}>Small</Text>

                            <Text large>Another large</Text>
                            <Text>Another medium</Text>
                            <Text small>Another small</Text>
                        </ThemeProvider>
                    </section>
                )
            })}
        </div>
    )
}
