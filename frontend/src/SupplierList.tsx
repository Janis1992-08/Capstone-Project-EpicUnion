import React, { useEffect, useState } from 'react';
import { Supplier} from "./components/FrontendSchema.ts";
import { Link } from 'react-router-dom';
import Modal from "./components/Modal.tsx";
import {getSuppliers} from "./api/SupplierService.ts";
import AddSupplierForm from "./components/supplierComponents/AddSupplierForm.tsx";
import "./styling/SupplierList.css"

export default function SupplierList() {
    const [suppliers, setSuppliers] = useState<Supplier[]>([]);
    const [isVisible, setIsVisible] = useState(false);
    const [filter, setFilter] = useState<string>('');

    const fetchSuppliers = () => {
        getSuppliers().then(response => setSuppliers(response.data))
            .catch(error => console.error('Error fetching suppliers:', error));
    };

    useEffect(() => {
        fetchSuppliers();
    }, []);

    const closeModal = () => setIsVisible(false);
    const openModal = () => setIsVisible(true);

    const handleSupplierAdded = () => {
        fetchSuppliers();
        closeModal();
    };

    const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFilter(event.target.value.toLowerCase());
    };

    const filteredSuppliers = suppliers.filter(supplier => {
        return supplier.name.toLowerCase().includes(filter) ||
            supplier.description.toLowerCase().includes(filter) ||
            supplier.contactEmail.toLowerCase().includes(filter) ||
            supplier.contactPhone.toLowerCase().includes(filter) ||
            supplier.contactAddress.toLowerCase().includes(filter);
    });

    return (
        <div className="supplier-list">
            <div className="supplier-list__header">
            <h1 className="supplier-list__title">Suppliers</h1>
            </div>
            <button className="supplier-list__button" onClick={openModal}>Add New Supplier</button>
            <Modal isVisible={isVisible} onClose={closeModal}>
                <AddSupplierForm onSave={handleSupplierAdded} />
            </Modal>
            <div >
            <input
                type="text"
                placeholder="Filter suppliers..."
                value={filter}
                onChange={handleFilterChange}
            />
            </div>
            <ul className="supplier-list__list">
                {filteredSuppliers.map(supplier => (
                    <li key={supplier.id} className="supplier-list__list-item">
                        <Link to={`/suppliers/${supplier.id}`}>
                            <h2 className="supplier-list__supplier-title">{supplier.name} </h2>
                            <p className="supplier-list__supplier-description">{supplier.description}</p>
                            <p className="supplier-list__supplier-info">Costs: {supplier.costs}</p>
                            <p className="supplier-list__supplier-info">Delivery Date: {supplier.deliveryDate}</p>
                            <p className="supplier-list__supplier-info">Email: {supplier.contactEmail}</p>
                        </Link>
                    </li>
                ))}
            </ul>
            <Link className="supplier-list__back-link" to="/homepage">Back to Home Page</Link>
        </div>
    );
}