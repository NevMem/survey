import { useState } from "react";
import { useNavigate } from "react-router-dom";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import SpacedColumn from "../../app/layout/SpacedColumn";
import TextButton from "../../components/button/TextButton";
import Text from "../../components/text/Text";
import { Project } from "../../data/exported";
import { ProjectSurveysWrapper, ProjectInfoWrapper } from "./views";

interface ProjectCardProps {
    project: Project,
    richCard?: boolean,
    defaultExpanded?: boolean,
};

const ProjectCard = (props: ProjectCardProps) => {
    const { project, richCard } = props;

    const [expanded, setExpanded] = useState(props.richCard || props.defaultExpanded);

    const MoreInfoView = () => {
        const navigate = useNavigate();

        return (
            <SpaceAroundRow>
                <TextButton onClick={() => navigate(`/project/${project.id}`)}>Больше информации</TextButton>
            </SpaceAroundRow>
        );
    };

    const CardHeader = () => {
        if (richCard) {
            return (
                <Text large>{project.name}</Text>
            );
        } else {
            return (
                <SpaceBetweenRow>
                    <Text large>{project.name}</Text>
                    <TextButton onClick={() => setExpanded(!expanded)}>{expanded ? 'Свернуть' : 'Развернуть'}</TextButton>
                </SpaceBetweenRow>
            );
        }
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <CardHeader />
                {(expanded || richCard) && <ProjectSurveysWrapper project={project} />}
                {(expanded || richCard) && <ProjectInfoWrapper project={project} />}
                {(expanded && !richCard) && <MoreInfoView />}
            </SpacedColumn>
        </OutlinedCard>
    );
};

export default ProjectCard;
