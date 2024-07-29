import {Link, useNavigate, useParams} from "react-router-dom";
import {Guest, Task} from "./components/FrontendSchema.ts";
import {useEffect, useState} from "react";
import './styling/GuestDetail.css';
import UpdateGuestForm from "./components/guestComponents/UpdateGuestForm.tsx";
import Modal from "./components/Modal.tsx";
import {assignTaskToGuest, deleteGuest, getGuestById} from "./api/GuestService.ts";
import {getTasks} from "./api/TaskService.ts";


export default function GuestDetail() {
const { id } = useParams<{id: string}>();
const [guest, setGuest] = useState<Guest | null>(null);
const [tasks, setTasks] = useState<Task[]>([]);
const navigate = useNavigate();
const [isVisible, setIsVisible] = useState(false);


    const fetchGuest = () => {
        if (id) {
            getGuestById(id).then(response => setGuest(response.data));
        }
        getTasks().then(response => setTasks(response.data));
    }

    useEffect(() => {
        fetchGuest();
    }, [id]);

    if (!guest) {
        return <p>Loading...</p>
    }

    const handleTaskAssignment = (taskId: string) => {
        if (id) {
            assignTaskToGuest(id, taskId)
                .then(() => {
                    fetchGuest();
                })
                .catch(error => {
                    console.error('Error assigning task to guest:', error);
                    alert('Failed to assign task. Please try again.');
                });
        }
    }

    const handleDelete = () => {
        if (id) {
            deleteGuest(id).then(() => {
                navigate('/guests');
            })
        }
    }

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleGuestUpdate = () => {
        fetchGuest();
        closeModal();

    }

    console.log(guest.assignedTasks);

return (
    <div className="guest-detail">
        <h2>Guest details</h2>
        <p><strong>Name:</strong> {guest.name}</p>
        <p><strong>Email:</strong> {guest.email}</p>
        <p><strong>RSVP Status:</strong> {guest.rsvpStatus}</p>
        <p><strong>Notes:</strong> {guest.notes}</p>
        <h2>Tasks: </h2>
        <ul>
            {(guest.assignedTasks || []).map(taskId => {
                const task = tasks.find(t => t.id === taskId);
                return task ? (
                    <li key={task.id}>{task.title} - {task.taskStatuses}</li>
                ) : null;
            })}
        </ul>
        <div>
            <h3>Assign Task</h3>
            <select onChange={(e) => handleTaskAssignment(e.target.value)} defaultValue="">
                <option value="" disabled>Select a task</option>
                {tasks.map(task => (
                    <option key={task.id} value={task.id}>
                        {task.title}
                    </option>
                ))}
            </select>
        </div>
        <button onClick={handleDelete}>Delete Guest</button>
        <button onClick={openModal}>Update Guest</button>
        <Modal isVisible={isVisible} onClose={closeModal}>
            <UpdateGuestForm guest={guest} onGuestUpdate={handleGuestUpdate}/>
        </Modal>
        <br/>
        <Link to={"/guests"}>Back to Guest List</Link>
    </div>
);
}