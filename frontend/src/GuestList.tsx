import {Guest, Task} from "./components/FrontendSchema.ts";
import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import './styling/GuestList.css';
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

    const filteredGuests = guests.filter(guest => {
        const assignedTaskTitles = guest.assignedTasks
            .map(taskId => tasks.find(task => task.id === taskId)?.title)
            .filter(Boolean)
            .join(', ')
            .toLowerCase();

        return guest.name.toLowerCase().includes(filter.toLowerCase()) ||
            guest.email.toLowerCase().includes(filter.toLowerCase()) ||
            guest.rsvpStatus.toLowerCase().includes(filter.toLowerCase()) ||
            assignedTaskTitles.includes(filter.toLowerCase());
    });


    return (
        <div className="guest-list">
            <div className="guest-list__header">
                <h1 className="guest-list__title">Gästeliste</h1>
            </div>
            <button className="guest-list__button" onClick={openModal}>ADD New Guest</button>
            <div className="guest-list__modal">
                <Modal isVisible={isVisible} onClose={closeModal}>
                    <AddGuestForm onGuestAdded={handleGuestAdded} />
                </Modal>
            </div>
            <div >
                <input
                    type="text"
                    placeholder="Filter guests..."
                    value={filter}
                    onChange={handleFilterChange}
                />
            </div>
            <ul className="guest-list__list">
                {filteredGuests.map(guest => (
                    <li className="guest-list__item" key={guest.id}>
                        <Link className="guest-list__item-link" to={`/guests/${guest.id}`}>
                            <div className="guest-list__guest-info">
                                <div className="guest-list__guest-name">Name: {guest.name}</div>
                                <div className="guest-list__guest-contact">Contact: {guest.email}</div>
                                <div className="guest-list__guest-status">Status: {guest.rsvpStatus}</div>
                                <p className="guest-list__guest-notes">{guest.notes}</p>
                            </div>
                            <ul className="guest-list__tasks">
                                {(guest.assignedTasks || []).map(assignedTaskId => {
                                    const task = tasks.find(t => t.id === assignedTaskId);
                                    return task ? (
                                        <li className="guest-list__task-item" key={task.id}>{task.title}</li>
                                    ) : null;
                                })}
                            </ul>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link className="guest-list__back-link" to="/homepage">Back to Home Page</Link>
        </div>
    );
}