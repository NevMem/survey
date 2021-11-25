import { ThemeProvider } from 'styled-components';
import GeneralButton from '../../components/button/GeneralButton';
import Loader from '../../components/loader/Loader';
import themes from '../../theme/themes';
import Text from '../../components/text/Text';
import SpaceBetweenRow from '../../app/layout/SpaceBetweenRow';

export default function DemoPage() {
    return (
        <div>
            {themes.map((theme) => {
                return (
                    <section>
                        <ThemeProvider theme={theme}>
                            <GeneralButton>Primary</GeneralButton>
                            <GeneralButton secondary>Secondary</GeneralButton>

                            <Text header>Header</Text>
                            <Text large>Large</Text>
                            <Text>Medium</Text>
                            <Text small>Small</Text>

                            <Loader small />
                            <Loader />
                            <Loader large />

                            <SpaceBetweenRow>
                                <Loader large />
                                <Text large>Large</Text>
                                <GeneralButton secondary>Button</GeneralButton>
                            </SpaceBetweenRow>
                        </ThemeProvider>
                    </section>
                )
            })}
        </div>
    )
}
