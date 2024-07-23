import { ReactNode } from "react";
import "../styling/Modal.css";

interface Modal {
    isVisible: boolean;
    onClose: () => void;
    children: ReactNode;
}

export default function Modal({isVisible, onClose, children}: Readonly<Modal>) {
    return (
        <>
            {isVisible &&
                <div className="modal">
                    <div className="modal-dialog">
                        {children}
                        <button onClick={() => onClose()}>Close</button>
                    </div>
                </div>
            }
        </>
    )
}