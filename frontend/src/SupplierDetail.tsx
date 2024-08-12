import {useState, useEffect, useCallback} from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Supplier} from "./components/FrontendSchema.ts";
import Modal from "./components/Modal.tsx";
import ConfirmModal from "./components/ConfirmModal.tsx";
import {deleteSupplier, getSupplierById} from "./api/SupplierService.ts";
import UpdateSuppliersForm from "./components/supplierComponents/UpdateSuppliersForm.tsx";
import "./styling/SupplierDetail.css";


export default function SupplierDetail() {
    const { id } = useParams<{ id: string }>();
    const [supplier, setSupplier] = useState<Supplier | null>(null);
    const navigate = useNavigate();
    const [isVisible, setIsVisible] = useState(false);
    const [isConfirmVisible, setIsConfirmVisible] = useState(false);

    const fetchSupplier = useCallback(() => {
        if (id) {
            getSupplierById(id).then(response => setSupplier(response.data));
        }
    }, [id]);


    useEffect(() => {
        fetchSupplier();
    }, [fetchSupplier]);

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

    return (
        <>
            <div className="supplier-detail">
                <div className="supplier-detail__header">
                    <h1 className="supplier-detail__title">Supplier Details</h1>
                </div>
                <ul className="supplier-detail__list">
                    <li className="supplier-detail__list-item" key={supplier.id}>
                        <h2 className="supplier-detail__supplier-title" >Name: {supplier.name}</h2>
                        <p className="supplier-detail__supplier-description" >Description: {supplier.description}</p>
                        <p className="supplier-detail__supplier-info" >Website: {supplier.websiteUrl}</p>
                        <p className="supplier-detail__supplier-info" >Costs: {supplier.costs}</p>
                        <p className="supplier-detail__supplier-info" >Delivery Date: {supplier.deliveryDate}</p>
                        <p className="supplier-detail__supplier-info" >Phone: {supplier.contactPhone}</p>
                        <p className="supplier-detail__supplier-info" >Email {supplier.contactEmail}</p>
                        <p className="supplier-detail__supplier-info" >Address {supplier.contactAddress}</p>
                            <button className="supplier-detail__button" onClick={openModal}>Update Supplier</button>
                            <Modal isVisible={isVisible} onClose={closeModal}>
                                <UpdateSuppliersForm initialSupplier={supplier} onSave={handleSupplierUpdate}/>
                            </Modal>
                            <button className="supplier-detail__button supplier-detail__button--delete" onClick={handleDelete}>Delete Supplier</button>
                    </li>
                </ul>
            </div>
            <ConfirmModal isVisible={isConfirmVisible} onClose={() => setIsConfirmVisible(false)} onConfirm={confirmDelete} message={"Are you sure you want to delete this supplier?"} />
            <Link className="supplier-detail__back-link" to="/suppliers">Back to Supplier List</Link>
        </>
    );
}