import GeneralNavbar from "../../components/navbar/GeneralNavbar";
import Text from "../../components/text/Text";
import ThemePicker from "../../components/theme/ThemePicker";
import SpaceBetweenRow from "../layout/SpaceBetweenRow";
import Logo from "../logo/Logo";

const AppNavbar = () => {
    return (
        <GeneralNavbar>
            <SpaceBetweenRow>
                <SpaceBetweenRow>
                    <Logo />
                    <Text>Survey<span style={{fontSize: '16px'}}>&beta;</span></Text>
                </SpaceBetweenRow>
                <ThemePicker />
            </SpaceBetweenRow>
        </GeneralNavbar>
    )
}

export default AppNavbar;
