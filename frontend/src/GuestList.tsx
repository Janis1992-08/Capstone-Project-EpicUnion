import {Guest, Task} from "./components/FrontendSchema.ts";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import './styling/GuestList.css';
import AddGuestForm from "./components/AddGuestForm.tsx";
import Modal from "./components/Modal.tsx";
import {getGuests} from "./api/GuestService.ts";
import {getTasks} from "./api/TaskService.ts";



export default function GuestList() {
    const [guests, setGuests] = useState<Guest[]>([]);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [isVisible, setIsVisible] = useState(false);


    const fetchGuests = () => {
        getGuests().then(response => setGuests(response.data));
        getTasks().then(response => setTasks(response.data));
    }

    useEffect(() => {
        fetchGuests();
    }, []);

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleGuestAdded = () => {
        fetchGuests();
        closeModal();
    };



    return (
        <div className="guest-list">
            <button onClick={openModal}>ADD New Guest</button>
            <Modal isVisible={isVisible} onClose={closeModal}>
                <AddGuestForm onGuestAdded={handleGuestAdded}/>
            </Modal>
            <h1>Gästeliste</h1>
            <ul>
                {guests.map(guest => (
                    <li key={guest.id}>
                        <Link to={`/guests/${guest.id}`}>
                            <div>Name: {guest.name}</div>
                            <div>Contact: {guest.email}</div>
                            <div>Status: {guest.rsvpStatus}</div>
                            <p>{guest.notes}</p>
                            <ul>
                                {(guest.taskIds || []).map(taskId => {
                                    const task = tasks.find(t => t.id === taskId);
                                    return task ? (
                                        <li key={task.id}>{task.title}</li>
                                    ) : null;
                                })}
                            </ul>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link to={"/"}>Back to Home Page</Link>
        </div>
    )

}