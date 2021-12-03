import { useState } from "react";
import styled from "styled-components";
import GeneralButton from "../../components/button/GeneralButton";


const DebugPanelContainer = styled.div`
    position: fixed;
    left: 24px;
    bottom: 24px;
    display: flex;
    flex-direction: row;
`;

const DebugPanelCard = styled.div`
    display: flex;
    flex-direction: column;
    padding: 16px;
    border-radius: 12px;
    background-color: ${props => props.theme.withAlpha(200).secondaryBackground};
`;

const DebugPanel = () => {
    const [openedPanel, setOpenedPanel] = useState(false);

    const togglePanel = () => {
        setOpenedPanel(!openedPanel);
    };

    if (openedPanel) {
        return (
            <DebugPanelContainer>
                <DebugPanelCard>

                </DebugPanelCard>
            </DebugPanelContainer>
        );
    }

    return (
        <DebugPanelContainer>
            <GeneralButton secondary onClick={togglePanel}>Debug panel</GeneralButton>
        </DebugPanelContainer>
    );
};

export default DebugPanel;
