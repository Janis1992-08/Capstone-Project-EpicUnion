import "../styling/ConfirmModal.css"

interface ConfirmModalProps {
    isVisible: boolean;
    onClose: () => void;
    onConfirm: () => void;
    message: string;
}

export default function ConfirmModal({isVisible, onClose, onConfirm, message}: Readonly<ConfirmModalProps>) {
    if (!isVisible) return null;

    return (
        <div className={"confirm-modal"}>
            <div className={"confirm-modal__content"}>
                <p>{message}</p>
                <div className={"confirm-modal__actions"}>
                    <button className={"confirm-modal__button"} onClick={onConfirm}>Yes</button>
                    <button className={"confirm-modal__button"} onClick={onClose}>No</button>
                </div>
            </div>
        </div>
    )
}