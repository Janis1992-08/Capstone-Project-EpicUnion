import { ReactNode } from "react";
import "../styling/Modal.css";

interface ModalProps {
    isVisible: boolean;
    onClose: () => void;
    children: ReactNode;
}



export default function Modal({ isVisible, onClose, children }: ModalProps) {
    return (
        <>
            {isVisible && (
                <div className="modal">
                    <div className="modal-dialog">
                        {children}
                        <button className="modal-close-btn" onClick={onClose}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}