import React, {useState, useEffect, useCallback} from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import {Supplier, Task} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import ConfirmModal from "./components/ConfirmModal.tsx";
import {deleteSupplier, getSupplierById} from "./api/SupplierService.ts";
import UpdateSuppliersForm from "./components/supplierComponents/UpdateSuppliersForm.tsx";
import './styling/globals/DetailsPages.css';
import {getTasks} from "./api/TaskService.ts";


export default function SupplierDetail() {
    const { id } = useParams<{ id: string }>();
    const [supplier, setSupplier] = useState<Supplier | null>(null);
    const [tasks, setTasks] = useState<Task[]>([]);
    const navigate = useNavigate();
    const [isVisible, setIsVisible] = useState(false);
    const [isConfirmVisible, setIsConfirmVisible] = useState(false);

    const fetchSupplier = useCallback(() => {
        if (id) {
            getSupplierById(id).then(response => setSupplier(response.data));
        }
    }, [id]);

    const fetchTasks = useCallback(() => {
        getTasks()
            .then(response => setTasks(response.data))
            .catch(error => console.error(error));
    }, []);


    useEffect(() => {
        fetchSupplier();
        fetchTasks();
    }, [fetchSupplier, fetchTasks]);

    if (!supplier) {
        return <p>Loading...</p>;
    }

    const handleDelete = () => {
        setIsConfirmVisible(true);
    };


    const confirmDelete = () => {
        if (id) {
            deleteSupplier(id)
                .then(() => navigate('/suppliers'))
                .catch(error => {
                    console.error('Error deleting guest:', error);
                    alert('Failed to delete guest. Please try again.');
                });
        }
    }

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleSupplierUpdate = () => {
        fetchSupplier();
        closeModal();
    };

    const getTaskNames = (tasksIds: string[]): React.ReactElement => {
        if (tasksIds.length === 0) return <span>Unassigned</span>;

        return (
            <>
                {tasksIds.map((tasksId, index) => {
                    const task = tasks.find(t => t.id === tasksId);
                    if (!task) return null;

                    return (
                        <span key={tasksId}>
                                <Link to={`/tasks/${tasksId}`}>
                                    {task.title}
                                </Link>
                            {index < tasksIds.length - 1 && ', '}
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
                    <h1 className="details-pages__title">Supplier Details</h1>
                </div>
                <ul className="details-pages__list">
                    <li className="details-pages__list-item" key={supplier.id}>
                        <h2 className="details-pages__list-title">Name: {supplier.name}</h2>
                        <p className="details-pages__list-description">Description: {supplier.description}</p>
                        <p className="details-pages__list-info">Website: {supplier.websiteUrl}</p>
                        <p className="details-pages__list-info">Costs: {supplier.costs}</p>
                        <p className="details-pages__list-info">Delivery Date: {supplier.deliveryDate}</p>
                        <p className="details-pages__list-info">Phone: {supplier.contactPhone}</p>
                        <p className="details-pages__list-info">Email: {supplier.contactEmail}</p>
                        <p className="details-pages__list-info">Address: {supplier.contactAddress}</p>
                        <p className="details-pages__list-info">Tasks: {getTaskNames(supplier.assignedTasks)}</p>
                        <button className="details-pages__button" onClick={openModal}>Update Supplier</button>
                        <Modal isVisible={isVisible} onClose={closeModal}>
                            <UpdateSuppliersForm initialSupplier={supplier} onSave={handleSupplierUpdate} tasks={tasks}/>
                        </Modal>
                        <button className="details-pages__button details-pages__button--delete"
                                onClick={handleDelete}>Delete Supplier
                        </button>
                    </li>
                </ul>
            </div>
            <ConfirmModal isVisible={isConfirmVisible} onClose={() => setIsConfirmVisible(false)} onConfirm={confirmDelete} message={"Are you sure you want to delete this supplier?"} />
            <Link className="details-pages__back-link" to="/suppliers">Back to Supplier List</Link>
        </>
    );
}