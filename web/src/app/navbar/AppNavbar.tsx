import { useContext } from "react";
import { ThemeContext } from "styled-components";
import GeneralNavbar from "../../components/navbar/GeneralNavbar";
import Text from "../../components/text/Text";
import ThemePicker from "../../components/theme/ThemePicker";
import useNavigator from "../../pages/navigation";
import { Theme } from "../../theme/theme";
import SpaceBetweenRow from "../layout/SpaceBetweenRow";
import Logo from "../logo/Logo";

const AppNavbar = () => {
    const theme: Theme = useContext(ThemeContext);

    const navigator = useNavigator();

    const goHome = () => {
        navigator.home();
    };

    return (
        <GeneralNavbar>
            <SpaceBetweenRow>
                <SpaceBetweenRow>
                    <Logo onClick={goHome} />
                    <Text onClick={goHome} style={{color: theme.background, cursor: 'pointer'}}>Survey<span style={{fontSize: '16px'}}>&beta;</span></Text>
                </SpaceBetweenRow>
                <ThemePicker />
            </SpaceBetweenRow>
        </GeneralNavbar>
    )
}

export default AppNavbar;
