import styled from "styled-components";

const SmallText = styled.p`
    font-size: 0.7em;
    margin: 0;
`;

const MediumText = styled.p`
    font-size: 1em;
    margin: 0;
`;

const LargeText = styled.p`
    font-size: 1.3em;
    margin: 0;
`;

enum TextStyle {
    Small,
    Medium,
    Large,
};

const Text = (props: {children: any | undefined, style: TextStyle | undefined}) => {
    const style: TextStyle = props.style ?? TextStyle.Medium
    switch (style) {
        case TextStyle.Large:
            return <LargeText>{props.children}</LargeText>;
        case TextStyle.Medium:
            return <MediumText>{props.children}</MediumText>;
        case TextStyle.Small:
            return <SmallText>{props.children}</SmallText>;
    }
}

export default Text;

export {
    TextStyle
};
