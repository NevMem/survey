import { useState } from 'react';
import styled from 'styled-components';

const Modal = styled.div`
    margin: 15% auto;
    width: 600px;
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

const ModalFade = styled.div<{ open: boolean }>`
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.3);
    display: ${props => props.open ? 'block' : 'none'};
    box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.7);
`;

interface ModalState {
    open: () => void;
    close: () => void;
    toggle: () => void;
    opened: boolean;
};

const useModalState = (): ModalState => {
    const [open, setOpen] = useState(false);

    return {
        open: () => { setOpen(true) },
        close: () => { setOpen(false) },
        toggle: () => { setOpen(!open) },
        opened: open,
    };
};

const ModalView = (props: { state: ModalState, children: any }) => {
    return (
        <ModalFade open={props.state.opened} onClick={() => props.state.close()}>
            <Modal onClick={(event) => {event.preventDefault(); event.stopPropagation();}}>
                {props.children}
            </Modal>
        </ModalFade>
    );
};

export default Modal;
export {
    ModalHeader,
    ModalBody,
    ModalActions,
    ModalView,

    useModalState,
};

export type {
    ModalState,
};
