import {Link, useNavigate, useParams} from "react-router-dom";
import {Guest} from "./components/FrontendSchema.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import './styling/GuestDetail.css';
import UpdateGuestForm from "./components/UpdateGuestForm.tsx";
import Modal from "./components/Modal.tsx";


export default function GuestDetail() {
const { id } = useParams<{id: string}>();
const [guest, setGuest] = useState<Guest | null>(null);
const navigate = useNavigate();
const [isVisible, setIsVisible] = useState(false);



    const fetchGuest = () => {
        axios.get(`/api/guests/${id}`)
            .then(response => setGuest(response.data))
            .catch(error => console.error(error));
    }

    useEffect(() => {
        fetchGuest();
    }, []);

    if (!guest) {
        return <p>Loading...</p>
    }

    const handleGuestDelete = async (id: string) => {
        try {
            await axios.delete(`/api/guests/${id}`);
            navigate('/');
        } catch (error) {
            console.error(error);
        }
    }

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleGuestUpdate = () => {
        fetchGuest();
        closeModal();

    }


return (
    <div className="guest-detail">
        <h2>Guest details</h2>
        <p><strong>Name:</strong> {guest.name}</p>
        <p><strong>Email:</strong> {guest.email}</p>
        <p><strong>RSVP Status:</strong> {guest.rsvpStatus}</p>
        <p><strong>Notes:</strong> {guest.notes}</p>
        <button onClick={() => handleGuestDelete(guest.id)}>Delete Guest</button>
        <button onClick={openModal}>Update Guest</button>
        <Modal isVisible={isVisible} onClose={closeModal}>
            <UpdateGuestForm guest={guest} onGuestUpdate={handleGuestUpdate}/>
        </Modal>
        <br/>
        <Link to={"/"}>Back to Guest List</Link>
    </div>
);
}