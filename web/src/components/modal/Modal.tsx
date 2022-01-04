import styled from 'styled-components';

const Modal = styled.div`
    margin: 15% auto;
    width: 400px;
    background-color: ${props => props.theme.background};
    border-radius: 8px;
    padding: 16px;
    clickable: true;
    z-index: 2;
`;

const ModalHeader = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
`;

const ModalBody = styled.div`
    display: flex;
    flex-direction: column;
    padding-top: 20px;
    padding-bottom: 20px;
`;

const ModalActions = styled.div`
    display: flex;
    flex-direction: row-reverse;
    column-gap: 10px;
`;

export default Modal;
export {
    ModalHeader,
    ModalBody,
    ModalActions,
};
