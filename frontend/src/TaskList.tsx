import React, { useEffect, useState } from 'react';
import { getTasks} from "./api/TaskService.ts";
import {Guest, Supplier, Task} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import AddTaskForm from "./components/taskComponents/AddTaskForm.tsx";
import {Link} from "react-router-dom";
import {getGuests} from "./api/GuestService.ts";
import './styling/TaskList.css';
import {getSuppliers} from "./api/SupplierService.ts";


export default function TaskList() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [guests, setGuests] = useState<Guest[]>([]);
    const [suppliers, setSuppliers] = useState<Supplier[]>([]);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [filter, setFilter] = useState<string>('');

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

    const fetchSuppliers = () => {
        getSuppliers()
            .then(response => setSuppliers(response.data))
            .catch(error => console.error(error));
    };

    useEffect(() => {
        fetchTasks();
        fetchGuests();
        fetchSuppliers();
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

    const getSupplierNames = (suppliersIds: string[]) => {
        return suppliersIds.map(suppliersId => {
            const supplier = suppliers.find(s => s.id === suppliersId);
            return supplier ? supplier.name : 'Unassigned';
        }).join(', ');
    };

    const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFilter(event.target.value);
    }

    const filteredTasks = tasks.filter(task =>
        task.title.toLowerCase().includes(filter.toLowerCase()) ||
        task.description.toLowerCase().includes(filter.toLowerCase()) ||
        task.dueDate.toLowerCase().includes(filter.toLowerCase()) ||
        task.taskStatus.toLowerCase().includes(filter.toLowerCase()) ||
        getGuestNames(task.assignedToGuests).toLowerCase().includes(filter.toLowerCase()) ||
        getSupplierNames(task.assignedToSuppliers).toLowerCase().includes(filter.toLowerCase())
    );


    return (
        <div className="task-list">
            <div className="task-list__header">
                <h1 className="task-list__title">Task List</h1>
            </div>
            <button className="task-list__button" onClick={() => setIsModalVisible(true)}>Add Task</button>
            <Modal isVisible={isModalVisible} onClose={() => setIsModalVisible(false)}>
                <AddTaskForm onSave={handleTaskAdded} guests={guests} suppliers={suppliers}/>
            </Modal>
            <div >
            <input
            type={"text"}
            placeholder={"Filter tasks..."}
            value={filter}
            onChange={handleFilterChange}
            />
            </div>
            <ul className="task-list__list">
                {filteredTasks.map(task => (
                    <li className="task-list__list-item" key={task.id}>
                        <Link to={`/tasks/${task.id}`}>
                            <h2 className="task-list__task-title">{task.title}</h2>
                            <p className="task-list__task-description">{task.description}</p>
                            <p className="task-list__task-info">Due Date: {task.dueDate}</p>
                            <p className="task-list__task-info">Status: {task.taskStatus}</p>
                            <p className="task-list__task-info">Assigned To: {getGuestNames(task.assignedToGuests)}</p>
                            <p className="task-list__task-info">Suppliers: {getSupplierNames(task.assignedToSuppliers)}</p>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link className="task-list__back-link" to="/homepage">Back to Home Page</Link>
        </div>
    );
}


