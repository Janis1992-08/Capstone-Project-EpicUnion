import { useEffect, useState } from 'react';
import { getTasks} from "./api/TaskService.ts";
import {Guest, Task} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import AddTaskForm from "./components/taskComponents/AddTaskForm.tsx";
import {Link} from "react-router-dom";
import {getGuests} from "./api/GuestService.ts";
import './styling/TaskManager.css';


export default function TaskManager() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [guests, setGuests] = useState<Guest[]>([]);
    const [isModalVisible, setIsModalVisible] = useState(false);

    const fetchTasks = () => {
        getTasks()
            .then(response => setTasks(response.data))
            .catch(error => console.error(error));
    };

    const fetchGuests = () => {
        getGuests()
            .then(response => setGuests(response.data))
            .catch(error => console.error(error));
    };

    useEffect(() => {
        fetchTasks();
        fetchGuests();
    }, []);

    const handleTaskAdded = () => {
        fetchTasks();
        setIsModalVisible(false);
    };

    const getGuestNames = (guestIds: string[]) => {
        return guestIds.map(guestId => {
            const guest = guests.find(g => g.id === guestId);
            return guest ? guest.name : 'Unassigned';
        }).join(', ');
    };


    return (
        <div className="task-manager">
            <div className="task-manager__header">
                <h1 className="task-manager__title">Task List</h1>
            </div>
            <button className="task-manager__button" onClick={() => setIsModalVisible(true)}>Add Task</button>
            <Modal isVisible={isModalVisible} onClose={() => setIsModalVisible(false)}>
                <AddTaskForm onSave={handleTaskAdded} guests={guests}/>
            </Modal>
            <ul className="task-manager__list">
                {tasks.map(task => (
                    <li className="task-manager__list-item" key={task.id}>
                        <Link to={`/tasks/${task.id}`}>
                            <h2 className="task-manager__task-title">{task.title}</h2>
                            <p className="task-manager__task-description">{task.description}</p>
                            <p className="task-manager__task-info">Due Date: {task.dueDate}</p>
                            <p className="task-manager__task-info">Status: {task.taskStatus}</p>
                            <p className="task-manager__task-info">Assigned To: {getGuestNames(task.assignedTo)}</p>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link className="task-manager__back-link" to="/">Back to Home Page</Link>
        </div>
    );
}


