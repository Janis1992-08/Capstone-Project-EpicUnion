import { Link, useNavigate, useParams } from "react-router-dom";
import { Guest, Task } from "./components/FrontendSchema.ts";
import {useCallback, useEffect, useState} from "react";
import './styling/GuestDetail.css';
import UpdateGuestForm from "./components/guestComponents/UpdateGuestForm.tsx";
import Modal from "./components/Modal.tsx";
import { assignTaskToGuest, deleteGuest, getGuestById, removeTaskFromGuest } from "./api/GuestService.ts";
import { getTasks } from "./api/TaskService.ts";

export default function GuestDetail() {
    const { id } = useParams<{ id: string }>();
    const [guest, setGuest] = useState<Guest | null>(null);
    const [tasks, setTasks] = useState<Task[]>([]);
    const navigate = useNavigate();
    const [isVisible, setIsVisible] = useState(false);

    const fetchGuest = useCallback(() => {
        if (id) {
            getGuestById(id).then(response => setGuest(response.data));
            getTasks().then(response => setTasks(response.data));
        }
    }, [id]);

    useEffect(() => {
        fetchGuest();
    }, [fetchGuest]);

    if (!guest) {
        return <p>Loading...</p>;
    }

    const handleTaskAssignment = (taskId: string) => {
        if (id) {
            assignTaskToGuest(id, taskId)
                .then(() => fetchGuest())
                .catch(error => {
                    console.error('Error assigning task to guest:', error);
                    alert('Failed to assign task. Please try again.');
                });
        }
    };

    const handleTaskRemoval = (taskId: string) => {
        if (id) {
            removeTaskFromGuest(id, taskId)
                .then(() => fetchGuest())
                .catch(error => {
                    console.error('Error removing task from guest:', error);
                    alert('Failed to remove task. Please try again.');
                });
        }
    };

    const handleDelete = () => {
        if (id) {
            deleteGuest(id).then(() => {
                navigate('/guests');
            });
        }
    };

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleGuestUpdate = () => {
        fetchGuest();
        closeModal();
    };

    return (
        <>
            <div className="guest-detail">
                <div className="guest-detail__header">
                    <h2 className="guest-detail__title">Guest Details</h2>
                </div>
                <div className="guest-detail__info">
                    <p><strong>Name:</strong> {guest.name}</p>
                    <p><strong>Email:</strong> {guest.email}</p>
                    <p><strong>RSVP Status:</strong> {guest.rsvpStatus}</p>
                    <p><strong>Notes:</strong> {guest.notes}</p>
                </div>
                <h2 className="guest-detail__tasks-title">Tasks:</h2>
                <ul className="guest-detail__tasks-list">
                    {(guest.assignedTasks || []).map(taskId => {
                        const task = tasks.find(t => t.id === taskId);
                        return task ? (
                            <li className="guest-detail__tasks-item" key={task.id}>
                                <Link to={`/tasks/${task.id}`} className="guest-detail__task-title">
                                    {task.title} - {task.taskStatus}
                                </Link>
                                <button className="guest-detail__task-button"
                                        onClick={() => handleTaskRemoval(task.id)}>Remove
                                </button>
                            </li>
                        ) : null;
                    })}
                </ul>
                <div className="guest-detail__assign-task">
                    <h3>Assign Task</h3>
                    <select onChange={(e) => handleTaskAssignment(e.target.value)} defaultValue="">
                        <option value="" disabled>Select a task</option>
                        {tasks.map(task => (
                            <option key={task.id} value={task.id}>
                                {task.title}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="guest-detail__buttons">
                    <button className="guest-detail__button guest-detail__button--delete" onClick={handleDelete}>Delete
                        Guest
                    </button>
                    <button className="guest-detail__button" onClick={openModal}>Update Guest</button>
                </div>
                <Modal isVisible={isVisible} onClose={closeModal}>
                    <UpdateGuestForm guest={guest} onGuestUpdate={handleGuestUpdate}/>
                </Modal>
            </div>
            <Link className="guest-detail__back-link" to={"/guests"}>Back to Guest List</Link>
        </>
    );
}
