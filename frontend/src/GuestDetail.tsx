import { Link, useNavigate, useParams } from "react-router-dom";
import { Guest, Task } from "./components/FrontendSchema.ts";
import React, {useCallback, useEffect, useState} from "react";
import './styling/globals/DetailsPages.css';
import UpdateGuestForm from "./components/guestComponents/UpdateGuestForm.tsx";
import Modal from "./components/Modal.tsx";
import { deleteGuest, getGuestById } from "./api/GuestService.ts";
import { getTasks } from "./api/TaskService.ts";
import ConfirmModal from "./components/ConfirmModal.tsx";

export default function GuestDetail() {
    const { id } = useParams<{ id: string }>();
    const [guest, setGuest] = useState<Guest | null>(null);
    const [tasks, setTasks] = useState<Task[]>([]);
    const navigate = useNavigate();
    const [isVisible, setIsVisible] = useState(false);
    const [isConfirmVisible, setIsConfirmVisible] = useState(false);

    const fetchGuest = useCallback(() => {
        if (id) {
            getGuestById(id).then(response => setGuest(response.data));
        }
    }, [id]);

    const fetchTasks = useCallback(() => {
        getTasks()
            .then(response => setTasks(response.data))
            .catch(error => console.error(error));
    }, []);

    useEffect(() => {
        fetchGuest();
        fetchTasks();
    }, [fetchGuest, fetchTasks]);

    if (!guest) {
        return <p>Loading...</p>;
    }


    const handleDelete = () => {
       setIsConfirmVisible(true);
    };

    const confirmDelete = () => {
        if (id) {
            deleteGuest(id)
                .then(() => navigate('/guests'))
                .catch(error => {
                    console.error('Error deleting guest:', error);
                    alert('Failed to delete guest. Please try again.');
                });
        }
    }

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleGuestUpdate = () => {
        fetchGuest();
        closeModal();
    };

    const getTaskNames = (tasksIds: string[]): React.ReactElement => {
        if (tasksIds.length === 0) return <span>Unassigned</span>;

        return (
            <>
                {tasksIds.map((tasksId, index) => {
                    const task = tasks.find(t => t.id === tasksId);
                    if (!task) return null;

                    return (
                        <span key={tasksId}>
                                <Link to={`/tasks/${tasksId}`} >
                                    {task.title}
                                </Link>
                            {index < tasksIds.length - 1 && ', '}
                            </span>
                    );
                })}
            </>
        );
    }

    return (
        <>
            <div className="details-pages__header">
                <h1 className="details-pages__title">Guest Details</h1>
            </div>
            <div className="details-pages">
                <ul className="details-pages__list">
                    <li className="details-pages__list-item" key={guest.id}>
                        <h2 className="details-pages__list-title">
                             {guest.firstName} {guest.lastName}</h2>
                        <p className="details-pages__list-info"><strong>Email:</strong> {guest.email}</p>
                        <p className="details-pages__list-info"><strong>Phone:</strong> {guest.phoneNumber}</p>
                        <p className="details-pages__list-info"><strong>RSVP Status:</strong> {guest.rsvpStatus}</p>
                        <p className="details-pages__list-info"><strong>Notes:</strong> {guest.notes}</p>
                        <p className="details-pages__list-info">Tasks: {getTaskNames(guest.assignedTasks)}</p>
                    </li>
                </ul>
                <button className="details-pages__button" onClick={openModal}>Update Guest</button>
                <Modal isVisible={isVisible} onClose={closeModal}>
                    <UpdateGuestForm guest={guest} onSave={handleGuestUpdate} tasks={tasks}/>
                </Modal>
                <button className="details-pages__button details-pages__button--delete"
                        onClick={handleDelete}>Delete Guest
                </button>
            </div>
            <Link className="details-pages__back-link" to={"/guests"}>Back to Guest List</Link>
            <ConfirmModal isVisible={isConfirmVisible} onClose={() => setIsConfirmVisible(false)}
                          onConfirm={confirmDelete} message={"Are you sure you want to delete this guest?"}/>
        </>
    );
}
