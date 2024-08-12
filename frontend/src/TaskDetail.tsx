import {Link, useNavigate, useParams} from "react-router-dom";
import React, {useCallback, useEffect, useState} from "react";
import {Guest, Supplier, Task} from "./components/FrontendSchema.ts";
import {deleteTask, getTaskById} from "./api/TaskService.ts";
import Modal from "./components/Modal.tsx";
import UpdateTaskForm from "./components/taskComponents/UpdateTaskForm.tsx";
import {getGuests} from "./api/GuestService.ts";
import './styling/globals/DetailsPages.css';
import ConfirmModal from "./components/ConfirmModal.tsx";
import {getSuppliers} from "./api/SupplierService.ts";

export default function TaskDetail() {
    const { id } = useParams<{ id: string }>();
    const [task, setTask] = useState<Task | undefined>();
    const [guests, setGuests] = useState<Guest[]>([]);
    const [suppliers, setSuppliers] = useState<Supplier[]>([]);
    const navigate = useNavigate();
    const [isModalVisible, setIsVisible] = useState(false);
    const [isConfirmVisible, setIsConfirmVisible] = useState(false);

    const fetchTask = useCallback(() => {
        if (id) {
            getTaskById(id)
                .then(response => setTask(response.data))
                .catch(error => console.error(error));
        }
    }, [id]);


    const fetchGuests = useCallback(() => {
        getGuests()
            .then(response => setGuests(response.data))
            .catch(error => console.error(error));
    }, []);

    const fetchSuppliers = useCallback(() => {
        getSuppliers()
            .then(response => setSuppliers(response.data))
            .catch(error => console.error(error));
    }, []);

    useEffect(() => {
        fetchTask();
        fetchGuests();
        fetchSuppliers();
    }, [fetchTask, fetchGuests, fetchSuppliers]);

    if (!task) {
        return <p>Loading...</p>;
    }

    const handleDelete = () => {
    setIsConfirmVisible(true);
    };

    const confirmDelete = () => {
        if (task) {
            deleteTask(task.id)
                .then(() => navigate('/tasks'))
                .catch(error => {
                    console.error('Error deleting task:', error);
                    alert('Failed to delete task. Please try again.');
                });
        }
    }

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleTaskUpdate = () => {
        fetchTask();
        closeModal();
    };

    const getGuestNames = (guestIds: string[]): React.ReactElement => {
        if (guestIds.length === 0) return <span>Unassigned</span>;

        return (
            <>
                {guestIds.map((guestId, index) => {
                    const guest = guests.find(g => g.id === guestId);
                    if (!guest) return null;

                    return (
                        <span key={guestId}>
                            <Link to={`/guests/${guestId}`} className="task-detail__guest-link">
                                {guest.firstName}
                            </Link>
                            {index < guestIds.length - 1 && ', '}
                        </span>
                    );
                })}
            </>
        );
    };

    const getSupplierNames = (suppliersIds: string[]): React.ReactElement => {
        if (suppliersIds.length === 0) return <span>Unassigned</span>;

        return (
            <>
                {suppliersIds.map((supplierId, index) => {
                    const supplier = suppliers.find(s => s.id === supplierId);
                    if (!supplier) return null;

                    return (
                        <span key={supplierId}>
                                <Link to={`/suppliers/${supplierId}`} className="task-detail__guest-link">
                                    {supplier.name}
                                </Link>
                            {index < suppliersIds.length - 1 && ', '}
                            </span>
                    );
                })}
            </>
        );
    }

    return (
        <>
        <div className="details-pages">
            <div className="details-pages__header">
                <h1 className="details-pages__title">Task Detail</h1>
            </div>
            <ul className="details-pages__list">
                <li className="details-pages__list-item" key={task.id}>
                    <h2 className="details-pages__list-title">{task.title}</h2>
                    <p className="details-pages__list-description">{task.description}</p>
                    <p className="details-pages__list-info ">Due Date: {task.dueDate}</p>
                    <p className="details-pages__list-info">Status: {task.taskStatus}</p>
                    <p className="details-pages__list-info">Assigned To: {getGuestNames(task.assignedToGuests)}</p>
                    <p className="details-pages__list-info">Suppliers: {getSupplierNames(task.assignedToSuppliers)}</p>
                    <button className="details-pages__button" onClick={openModal}>Update Task</button>
                    <Modal isVisible={isModalVisible} onClose={closeModal}>
                        <UpdateTaskForm initialTask={task} onSave={handleTaskUpdate} guests={guests} suppliers={suppliers}/>
                    </Modal>
                    <button className="details-pages__button details-pages__button--delete" onClick={handleDelete}>Delete
                    </button>
                </li>
            </ul>
        </div>
            <Link className="details-pages__back-link" to="/tasks">Back to Task List</Link>
            <ConfirmModal isVisible={isConfirmVisible} onClose={() => setIsConfirmVisible(false)} onConfirm={confirmDelete} message={"Are you sure you want to delete this task?"}/>

        </>
    );
}