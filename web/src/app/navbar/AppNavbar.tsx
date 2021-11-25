import GeneralNavbar from "../../components/navbar/GeneralNavbar";
import Text from "../../components/text/Text";
import Logo from "../logo/Logo";

const AppNavbar = () => {
    return (
        <GeneralNavbar>
            <Logo />
            <Text>Survey<span style={{fontSize: '16px'}}>&beta;</span></Text>
        </GeneralNavbar>
    )
}

export default AppNavbar;
