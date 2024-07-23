import {Guest} from "./components/FrontendSchema.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import './styling/GuestList.css';
import AddGuestForm from "./components/AddGuestForm.tsx";
import Modal from "./components/Modal.tsx";


export default function GuestList() {
    const [guests, setGuests] = useState<Guest[]>([]);
    const [isVisible, setIsVisible] = useState(false);


    const fetchGuests = () => {
        axios.get('/api/guests')
            .then(response => setGuests(response.data))
            .catch(error => console.error(error));
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
                    <>
                    <Link to={`/guests/${guest.id}`}>
                        <li key={guest.id}>
                            <div>Name: {guest.name}</div>
                            <div>Contact: {guest.email}</div>
                            <div>Status: {guest.rsvpStatus}</div>
                            <p>{guest.notes}</p>
                        </li>
                    </Link>
                    </>
                ))}
            </ul>
        </div>
    )

}