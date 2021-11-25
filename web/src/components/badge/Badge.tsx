import { useContext } from "react";
import styled, { ThemeContext } from "styled-components";

const StyledBadge = styled.div<{badgeTextColor: string, badgeBackgroundColor: string}>`
    padding: 8px;
    padding-left: 12px;
    padding-right: 12px;
    border-radius: 4px;
    background-color: ${props => props.badgeBackgroundColor};
    color: ${props => props.badgeTextColor};
`;

interface BadgeProps {
    success?: boolean
    error?: boolean
    warning?: boolean
    children: any
}

const Badge = (props: BadgeProps) => {
    const theme = useContext(ThemeContext);
    console.log(theme);
    if (props.error) {
        return <StyledBadge badgeBackgroundColor={theme.withAlpha(60).error} badgeTextColor={theme.error}>{props.children}</StyledBadge>;
    }
    if (props.success) {
        return <StyledBadge badgeBackgroundColor={theme.withAlpha(60).success} badgeTextColor={theme.success}>{props.children}</StyledBadge>;
    }
    if (props.warning) {
        return <StyledBadge badgeBackgroundColor={theme.withAlpha(60).warning} badgeTextColor={theme.warning}>{props.children}</StyledBadge>;
    }
    return <StyledBadge
            badgeBackgroundColor={theme.withAlpha(60).grey}
            badgeTextColor={theme.grey}
        >
            {props.children}
        </StyledBadge>;
};

export default Badge;
export type {
    BadgeProps,
};
