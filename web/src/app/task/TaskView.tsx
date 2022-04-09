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
    return (
        <CardWithShadow style={{marginRight: '8px'}}>{props.media.filename}</CardWithShadow>
    );
};


const TaskView = (props: { task: Task }) => {
    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <SpaceBetweenRow>
                    <TaskViewBadge taskState={props.task.state} />
                    <Text>Task id: {props.task.id}</Text>
                </SpaceBetweenRow>

                <Card>
                    <SpacedColumn rowGap={2}>
                        {props.task.log.map((elem, index) => {
                            return (
                                <Text key={index}>{elem.timestamp}: {elem.message}</Text>
                            );
                        })}
                    </SpacedColumn>
                </Card>

                <Row>
                    {props.task.outputs.map((elem, index) => {
                        return (
                            <TaskOutputView media={elem} key={index} />
                        )
                    })}
                </Row>
            </SpacedColumn>
        </OutlinedCard>
    );
};

export default TaskView;
