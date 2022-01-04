import { Fragment, useState } from "react";
import styled from "styled-components";
import GeneralButton from "../../components/button/GeneralButton";
import { Feature } from "../../service/experiments/data";
import { features } from "../../service/experiments/experiments";
import experimentsService from "../../service/experiments/ExperimentsService";
import SpaceBetweenRow from "../layout/SpaceBetweenRow";
import Text from "../../components/text/Text";


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
    const [enabled, setEnabled] = useState(experimentsService.isFeatureEnabled(props.feature));

    const onChange = (event: any) => {
        const newValue = event.target.checked;
        experimentsService.setFeatureEnabled(props.feature, newValue);
        setEnabled(!enabled);
    };

    return (
        <SpaceBetweenRow>
            <label htmlFor={'feature-' + props.feature.name}>{props.feature.name}</label>
            <input type='checkbox' id={'feature-' + props.feature.name} checked={enabled} onChange={onChange} />
        </SpaceBetweenRow>
    );
};

const FeaturesBlock = () => {
    return (
        <Fragment>
            {features.map(feature => {
                return <FeatureBlock key={feature.name} feature={feature} />;
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

                    <SpaceBetweenRow>
                        <Text large>Debug panel</Text>
                        <Text large onClick={togglePanel} style={{cursor: 'pointer'}}>&times;</Text>
                    </SpaceBetweenRow>                    

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
