import styled, { keyframes } from 'styled-components';

const animation = keyframes`
    0% { width: 20%; height: 20%; }
    100% { width: calc(100% - 8px); height: calc(100% - 8px); opacity: 0; }
`;

const SmallLoader = styled.div`
    width: 40px;
    height: 40px;
    animation-name: ${animation};
    animation-duration: 0.75s;
    animation-iteration-count: infinite;
    border: 4px solid ${props => props.theme.primary};
    border-radius: 50%;
`;

const LoaderContainer = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
`;

const Loader = (props: {large?: boolean, small?: boolean}) => {
    var size = 40;
    if (props.large) {
        size = 80;
    } else if (props.small) {
        size = 20;
    }
    return <LoaderContainer style={{width: size, height: size}}><SmallLoader /></LoaderContainer>;
};

export default Loader;
