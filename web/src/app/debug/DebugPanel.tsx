import { Fragment, useState } from "react";
import styled from "styled-components";
import GeneralButton from "../../components/button/GeneralButton";
import { Feature } from "../../service/experiments/data";
import { features } from "../../service/experiments/experiments";
import SpaceBetweenRow from "../layout/SpaceBetweenRow";


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

const FeatureBlock = (props: {feature: Feature}) => {
    return (
        <SpaceBetweenRow>
            <label htmlFor={'feature-' + props.feature.name}>{props.feature.name}</label>
            <input type='checkbox' id={'feature-' + props.feature.name} />
        </SpaceBetweenRow>
    );
};

const FeaturesBlock = () => {
    return (
        <Fragment>
            {features.map(feature => {
                return <FeatureBlock feature={feature} />;
            })}
        </Fragment>
    );
};

const DebugPanel = () => {
    const [openedPanel, setOpenedPanel] = useState(false);

    const togglePanel = () => {
        setOpenedPanel(!openedPanel);
    };

    if (openedPanel) {
        return (
            <DebugPanelContainer>
                <DebugPanelCard>
                    <FeaturesBlock />
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
