import { Media, Task, TaskState } from "../../data/exported";
import SpacedColumn from "../layout/SpacedColumn";
import Text from "../../components/text/Text";
import SpaceAroundRow from "../layout/SpaceAroundRow";
import Badge from "../../components/badge/Badge";
import OutlinedCard from "../card/OutlinedCard";
import SpaceBetweenRow from "../layout/SpaceBetweenRow";
import Card from "../card/Card";
import Row from "../layout/Row";
import styled from "styled-components";
import Column from "../layout/Column";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import GeneralButton from "../../components/button/GeneralButton";
import TextButton from "../../components/button/TextButton";


const TaskViewBadge = (props: { taskState: TaskState }) => {
    const { taskState } = props;

    if (taskState == TaskState.Success) {
        return <Badge success>{taskState}</Badge>;
    }
    if (taskState == TaskState.Error) {
        return <Badge error>{taskState}</Badge>;
    }
    return <Badge info>{taskState}</Badge>;
};

const CardWithShadow = styled.div`
    padding: 8px 16px;
    border-radius: 8px;
    background-color: ${props => props.theme.background};
    box-shadow: 0px 2px 6px ${props => props.theme.grey};
    cursor: pointer;
    transition: all ease-in 0.1s;

    &:hover {
        box-shadow: 0px 2px 16px ${props => props.theme.grey};
    }
`;

const TaskOutputView = (props: { media: Media }) => {
    const { media } = props;

    const navigate = useNavigate();

    const onClick = () => {
        window.open(media.url, '_blank')?.focus();
    };

    return (
        <CardWithShadow style={{marginRight: '8px'}} onClick={onClick}>{media.filename}</CardWithShadow>
    );
};


const TaskOutputsView = (props: { outputs: Media[] }) => {
    return (
        <SpacedColumn rowGap={8}>
            <Text>Outputs:</Text>
            <Row>
                {props.outputs.map((elem, index) => {
                    return (
                        <TaskOutputView media={elem} key={index} />
                    )
                })}
            </Row>
        </SpacedColumn>
    );
};


const TaskView = (props: { task: Task, expandedDefault?: boolean }) => {

    const [expanded, setExpanded] = useState(props.expandedDefault ?? false);

    const ExpandButton = (props: { expanded: boolean, setExpanded: (expanded: boolean) => void }) => {
        if (props.expanded) {
            return <TextButton secondary onClick={() => { props.setExpanded(false) }}>Скрыть</TextButton>;
        }
        return <TextButton onClick={() => { props.setExpanded(true) }}>Раскрыть</TextButton>
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <SpaceBetweenRow>
                    <TaskViewBadge taskState={props.task.state} />
                    <Text>Task id: {props.task.id}</Text>
                    <ExpandButton expanded={expanded} setExpanded={setExpanded} />
                </SpaceBetweenRow>

                {expanded && <Card>
                    <SpacedColumn rowGap={2}>
                        {props.task.log.map((elem, index) => {
                            return (
                                <Text key={index}>{elem.timestamp}: {elem.message}</Text>
                            );
                        })}
                    </SpacedColumn>
                </Card>}

                {expanded && <TaskOutputsView outputs={props.task.outputs} />}
            </SpacedColumn>
        </OutlinedCard>
    );
};

export default TaskView;
