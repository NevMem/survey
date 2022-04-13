import React from "react";
import styled from "styled-components";

const SmallText = styled.p`
    font-size: ${props => props.theme.smallTextSize};
    color: ${props => props.theme.foreground};
    margin: 0;
`;

const MediumText = styled.p`
    font-size: ${props => props.theme.mediumTextSize};
    color: ${props => props.theme.foreground};
    margin: 0;
`;

const LargeText = styled.p`
    font-size: ${props => props.theme.largeTextSize};
    color: ${props => props.theme.foreground};
    margin: 0;
`;

const HeaderText = styled.p`
    font-size: ${props => props.theme.headerTextSize};
    color: ${props => props.theme.foreground};
    margin: 0;
`;

const Text = (props: {
    children?: any,
    large?: boolean,
    small?: boolean,
    header?: boolean,
    style?: any,
    onClick?: React.MouseEventHandler<HTMLParagraphElement>
}) => {
    if (props.header) {
        return <HeaderText onClick={props.onClick} style={props.style}>{props.children}</HeaderText>
    }
    if (props.large) {
        return <LargeText onClick={props.onClick} style={props.style}>{props.children}</LargeText>;
    }
    if (props.small) {
        return <SmallText onClick={props.onClick} style={props.style}>{props.children}</SmallText>;
    }
    return <MediumText onClick={props.onClick} style={props.style}>{props.children}</MediumText>;
}

export default Text;
