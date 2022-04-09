import styled from "styled-components";

interface TextButtonProps {
    secondary?: boolean;
    children?: any;
    onClick?: React.MouseEventHandler<HTMLButtonElement>;
};

const PrimaryTextButton = styled.button`
    color: ${props => props.theme.primary};
    font-size: 1em;
    padding: 8px 18px;
    cursor: pointer;
    border-radius: 4px;
    border: 2px solid ${props => props.theme.background};
    background-color: ${props => props.theme.background};
    transition: all ease-in 0.2s;

    &:hover {
        color: ${props => props.theme.background};
        background-color: ${props => props.theme.primary};
        border-color: ${props => props.theme.primary};
    }
`;

const TextButton = (props: TextButtonProps) => {
    const { secondary, children, onClick } = props;
    
    return <PrimaryTextButton onClick={onClick}>{children}</PrimaryTextButton>
};

export default TextButton;
