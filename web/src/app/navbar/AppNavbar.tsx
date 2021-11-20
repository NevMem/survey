import GeneralNavbar from "../../components/navbar/GeneralNavbar";
import Text, { TextStyle } from "../../components/text/Text";
import Logo from "../logo/Logo";

const AppNavbar = () => {
    return (
        <GeneralNavbar>
            <Logo />
            <Text style={TextStyle.Medium}>Survey</Text>
        </GeneralNavbar>
    )
}

export default AppNavbar;
