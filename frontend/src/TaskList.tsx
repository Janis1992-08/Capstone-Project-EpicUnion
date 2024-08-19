import React, { useEffect, useState } from 'react';
import { getTasks} from "./api/TaskService.ts";
import {Guest, Supplier, Task, getTaskStatusLabel, formatDate} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import AddTaskForm from "./components/taskComponents/AddTaskForm.tsx";
import {Link} from "react-router-dom";
import {getGuests} from "./api/GuestService.ts";
import './styling/globals/ListPages.css';
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
            return guest ? guest.firstName : 'Unassigned';
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

    const filteredTasks = tasks.filter(task => {
        const statusLabel = getTaskStatusLabel(task.taskStatus) || '';
        return task.title.toLowerCase().includes(filter.toLowerCase()) ||
            task.description.toLowerCase().includes(filter.toLowerCase()) ||
            statusLabel.toLowerCase().includes(filter.toLowerCase());
    });


    return (
        <div className="list-pages">
            <div className="list-pages__header">
                <h1 className="list-pages__title">Task List</h1>
            </div>
            <button className="list-pages__button" onClick={() => setIsModalVisible(true)}>Add Task</button>
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
            <ul className="list-pages__list">
                {filteredTasks.map(task => (
                    <li className="list-pages__list-item" key={task.id}>
                        <Link to={`/tasks/${task.id}`}>
                            <h2 className="list-pages__list-title">{task.title}</h2>
                            <p className="list-pages__list-description">{task.description}</p>
                            <p className="list-pages__list-info">Due Date: {formatDate(task.dueDate)}</p>
                            <p className="list-pages__list-info">Status: {getTaskStatusLabel(task.taskStatus)}</p>
                            <p className="list-pages__list-info">Assigned To: {getGuestNames(task.assignedToGuests)}</p>
                            <p className="list-pages__list-info">Suppliers: {getSupplierNames(task.assignedToSuppliers)}</p>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link className="list-pages__back-link" to="/homepage">Back to Home Page</Link>
        </div>
    );
}

