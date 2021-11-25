import { ThemeProvider } from 'styled-components';
import GeneralButton from '../../components/button/GeneralButton';
import Loader from '../../components/loader/Loader';
import themes from '../../theme/themes';
import Text from '../../components/text/Text';

export default function DemoPage() {
    return (
        <div>
            {themes.map((theme) => {
                return (
                    <section>
                        <ThemeProvider theme={theme}>
                            <GeneralButton secondary={false}>Button</GeneralButton>
                            <GeneralButton secondary={true}>Button</GeneralButton>

                            <Text header>Header</Text>
                            <Text large>Large</Text>
                            <Text>Medium</Text>
                            <Text small>Small</Text>

                            <Loader small />
                            <Loader />
                            <Loader large />
                        </ThemeProvider>
                    </section>
                )
            })}
        </div>
    )
}
