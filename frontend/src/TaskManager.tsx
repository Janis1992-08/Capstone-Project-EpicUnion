import { useEffect, useState } from 'react';
import { getTasks} from "./api/TaskService.ts";
import {Guest, Task} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import AddTaskForm from "./components/taskComponents/AddTaskForm.tsx";
import {Link} from "react-router-dom";
import {getGuests} from "./api/GuestService.ts";


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
        <div>
            <button onClick={() => setIsModalVisible(true)}>Add Task</button>
            <Modal isVisible={isModalVisible} onClose={() => setIsModalVisible(false)}>
                <AddTaskForm onSave={handleTaskAdded} guests={guests} />
            </Modal>
            <h1>Task List</h1>
            <ul>
                {tasks.map(task => (
                    <li key={task.id}>
                        <Link to={`/tasks/${task.id}`}>
                            <h2>{task.title}</h2>
                            <p>{task.description}</p>
                            <p>Due Date: {task.dueDate}</p>
                            <p>Status: {task.taskStatuses}</p>
                            <p>Assigned To: {getGuestNames(task.assignedTo)}</p>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link to="/">Back to Home Page</Link>
        </div>
    );
}


