import {Link, useNavigate, useParams} from "react-router-dom";
import React, {useCallback, useEffect, useState} from "react";
import {Guest, Task} from "./components/FrontendSchema.ts";
import {deleteTask, getTaskById} from "./api/TaskService.ts";
import Modal from "./components/Modal.tsx";
import UpdateTaskForm from "./components/taskComponents/UpdateTaskForm.tsx";
import {getGuests} from "./api/GuestService.ts";
import './styling/TaskDetail.css';

export default function TaskDetail() {
    const { id } = useParams<{ id: string }>();
    const [task, setTask] = useState<Task | undefined>();
    const [guests, setGuests] = useState<Guest[]>([]);
    const navigate = useNavigate();
    const [isModalVisible, setIsVisible] = useState(false);

    const fetchTask = useCallback(() => {
        if (id) {
            getTaskById(id)
                .then(response => setTask(response.data))
                .catch(error => console.error(error));
        }
    }, [id]);


    const fetchGuests = useCallback(() => {
        getGuests()
            .then(response => setGuests(response.data))
            .catch(error => console.error(error));
    }, []);

    useEffect(() => {
        fetchTask();
        fetchGuests();
    }, [fetchTask, fetchGuests]);

    if (!task) {
        return <p>Loading...</p>;
    }

    const handleDelete = () => {
        if (id) {
            deleteTask(id)
                .then(() => navigate('/tasks'))
                .catch(error => console.error(error));
        }
    };

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleTaskUpdate = () => {
        fetchTask();
        closeModal();
    };

    const getGuestNames = (guestIds: string[]): React.ReactElement => {
        if (guestIds.length === 0) return <span>Unassigned</span>;

        return (
            <>
                {guestIds.map((guestId, index) => {
                    const guest = guests.find(g => g.id === guestId);
                    if (!guest) return null;

                    return (
                        <span key={guestId}>
                            <Link to={`/guests/${guestId}`} className="task-detail__guest-link">
                                {guest.name}
                            </Link>
                            {index < guestIds.length - 1 && ', '}
                        </span>
                    );
                })}
            </>
        );
    };

    return (
        <>
        <div className="task-detail">
            <div className="task-detail__header">
                <h1 className="task-detail__title">Task Detail</h1>
            </div>
            <ul className="task-detail__list">
                <li className="task-detail__list-item" key={task.id}>
                    <h2 className="task-detail__task-title">{task.title}</h2>
                    <p className="task-detail__task-description">{task.description}</p>
                    <p className="task-detail__task-info ">Due Date: {task.dueDate}</p>
                    <p className="task-detail__task-info">Status: {task.taskStatus}</p>
                    <p className="task-detail__task-info">Assigned To: {getGuestNames(task.assignedTo)}</p>
                    <button className="task-detail__button" onClick={openModal}>Update Task</button>
                    <Modal isVisible={isModalVisible} onClose={closeModal}>
                        <UpdateTaskForm initialTask={task} onSave={handleTaskUpdate} guests={guests}/>
                    </Modal>
                    <button className="task-detail__button task-detail__button--delete" onClick={handleDelete}>Delete
                    </button>
                </li>
            </ul>
        </div>
    <Link className="task-detail__back-link" to="/tasks">Back to Task List</Link>
    </>
    );
}