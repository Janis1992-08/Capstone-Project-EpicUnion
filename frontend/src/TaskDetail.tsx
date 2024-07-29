import {Link, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Guest, Task} from "./components/FrontendSchema.ts";
import {deleteTask, getTaskById} from "./api/TaskService.ts";
import Modal from "./components/Modal.tsx";
import UpdateTaskForm from "./components/taskComponents/UpdateTaskForm.tsx";
import {getGuests} from "./api/GuestService.ts";

export default function TaskDetail() {
    const { id } = useParams<{ id: string }>();
    const [task, setTask] = useState<Task | undefined>();
    const [guests, setGuests] = useState<Guest[]>([]);
    const navigate = useNavigate();
    const [isModalVisible, setIsVisible] = useState(false);

    const fetchTask = () => {
        if (id) {
            getTaskById(id)
                .then(response => setTask(response.data))
                .catch(error => console.error(error));
        } else {
            console.error('Task ID is undefined');
        }
    };


    const fetchGuests = () => {
        getGuests()
            .then(response => setGuests(response.data))
            .catch(error => console.error(error));
    };

    useEffect(() => {
        fetchTask();
        fetchGuests();
    }, [id]);

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

    const getGuestNames = (guestIds: string[]) => {
        return guestIds.map(guestId => {
            const guest = guests.find(g => g.id === guestId);
            return guest ? guest.name : 'Unassigned';
        }).join(', ');
    };

    return (
        <div>
            <h1>Task Detail</h1>
            <ul>
                <li key={task.id}>
                    <h2>{task.title}</h2>
                    <p>{task.description}</p>
                    <p>Due Date: {task.dueDate}</p>
                    <p>Status: {task.taskStatuses}</p>
                    <p>Assigned To: {getGuestNames(task.assignedTo)}</p>
                    <button onClick={openModal}>Update Task</button>
                    <Modal isVisible={isModalVisible} onClose={closeModal}>
                        <UpdateTaskForm initialTask={task} onSave={handleTaskUpdate} guests={guests} />
                    </Modal>
                    <button onClick={handleDelete}>Delete</button>
                </li>
            </ul>
            <Link to="/tasks">Back to Task List</Link>
        </div>
    );
}