import {Guest, Task} from "./components/FrontendSchema.ts";
import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import './styling/globals/ListPages.css';
import AddGuestForm from "./components/guestComponents/AddGuestForm.tsx";
import Modal from "./components/Modal.tsx";
import {getGuests} from "./api/GuestService.ts";
import {getTasks} from "./api/TaskService.ts";



export default function GuestList() {
    const [guests, setGuests] = useState<Guest[]>([]);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [isVisible, setIsVisible] = useState(false);
    const [filter, setFilter] = useState<string>('');

    const fetchGuests = () => {
        getGuests().then(response => setGuests(response.data));
        getTasks().then(response => setTasks(response.data));
    };

    useEffect(() => {
        fetchGuests();
    }, []);

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleGuestAdded = () => {
        fetchGuests();
        closeModal();
    };

    const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFilter(event.target.value);
    };

    const getTaskNames = (tasksIds: string[]) => {
        return tasksIds.map(tasksId => {
            const task = tasks.find(t => t.id === tasksId);
            return task ? task.title : 'Unassigned';
        }).join(', ');
    };

    const filteredGuests = guests.filter(guest => {
        const assignedTaskTitles = guest.assignedTasks
            .map(taskId => tasks.find(task => task.id === taskId)?.title)
            .filter(Boolean)
            .join(', ')
            .toLowerCase();

        return guest.firstName.toLowerCase().includes(filter.toLowerCase()) ||
            guest.lastName.toLowerCase().includes(filter.toLowerCase()) ||
            guest.email.toLowerCase().includes(filter.toLowerCase()) ||
            guest.rsvpStatus.toLowerCase().includes(filter.toLowerCase()) ||
            assignedTaskTitles.includes(filter.toLowerCase());
    });


    return (
        <div className="list-pages">
            <div className="list-pages__header">
                <h1 className="list-pages__title">Gästeliste</h1>
            </div>
            <button className="list-pages__button" onClick={openModal}>ADD New Guest</button>
                <Modal isVisible={isVisible} onClose={closeModal}>
                    <AddGuestForm onSave={handleGuestAdded} tasks={tasks} />
                </Modal>
            <div >
                <input
                    type="text"
                    placeholder="Filter guests..."
                    value={filter}
                    onChange={handleFilterChange}
                />
            </div>
            <ul className="list-pages__list">
                {filteredGuests.map(guest => (
                    <li className="list-pages__list-item" key={guest.id}>
                        <Link to={`/guests/${guest.id}`}>
                            <h2 className="list-pages__list-title"> {guest.firstName}  {guest.lastName}</h2>
                            <p className="list-pages__list-info">Contact: {guest.email}</p>
                            <p className="list-pages__list-info">Status: {guest.rsvpStatus}</p>
                            <p className="list-pages__list-info">{guest.notes}</p>
                            <p className="list-pages__list-info">Tasks: {getTaskNames(guest.assignedTasks)}</p>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link className="list-pages__back-link" to="/homepage">Back to Home Page</Link>
        </div>
    );
}