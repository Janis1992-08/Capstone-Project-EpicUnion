import { useEffect, useState } from 'react';
import {deleteTask, getTasks} from "./api/TaskService.ts";
import {Guest, Task} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import AddTaskForm from "./components/AddTaskForm.tsx";
import {Link} from "react-router-dom";
import {getGuests} from "./api/GuestService.ts";


export default function TaskManager() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [guests, setGuests] = useState<Guest[]>([]);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [currentTask, setCurrentTask] = useState<Task | null>(null);

    const fetchTasks = () => {
        getTasks()
            .then(response => setTasks(response.data))
            .catch(error => console.error(error));
    };

    const fetchGuest = () => {
        getGuests()
            .then(response => setGuests(response.data))
            .catch(error => console.error(error));

    }

    useEffect(() => {
        fetchTasks();
        fetchGuest();
    }, []);

    const handleTaskAdded = () => {
        fetchTasks();
        setIsModalVisible(false);
        setCurrentTask(null);
    };

    const handleEditTask = (task: Task) => {
        setCurrentTask(task);
        setIsModalVisible(true);
    };

    const handleDeleteTask = (id: string) => {
        deleteTask(id)
            .then(fetchTasks)
            .catch(error => console.error(error));
    };

    const getGuestName = (guestId: string) => {
        const guest = guests.find(guest => guest.id === guestId);
        return guest ? guest.name : 'Unassigned';
    }

    return (
        <div>
            <button onClick={() => {
                setCurrentTask(null);
                setIsModalVisible(true);
            }}>Add Task
            </button>
            <Modal isVisible={isModalVisible} onClose={() => setIsModalVisible(false)}>
                <AddTaskForm initialTask={currentTask} onSave={handleTaskAdded} />
            </Modal>
            <h1>Task List</h1>
            <ul>
                {tasks.map(task => (
                    <li key={task.id}>
                        <h2>{task.title}</h2>
                        <p>{task.description}</p>
                        <p>Due Date: {task.dueDate}</p>
                        <p>Status: {task.taskStatuses}</p>
                        <p>Assigned To: {getGuestName(task.assignedTo)}</p>
                        <button onClick={() => handleEditTask(task)}>Edit</button>
                        <button onClick={() => handleDeleteTask(task.id)}>Delete</button>
                    </li>
                ))}
            </ul>
            <Link to={"/"}>Back to Home Page</Link>
        </div>
    );
}


