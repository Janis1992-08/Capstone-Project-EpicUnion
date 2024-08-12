import { Supplier} from "../FrontendSchema.ts";
import React, {useState} from "react";
import {createSupplier} from "../../api/SupplierService.ts";
import {SupplierFormFields} from "./SupplierFormFields.tsx";

interface AddSupplierFormProps {
    onSave: () => void;
}


export default function AddSupplierForm({ onSave }: Readonly<AddSupplierFormProps>) {
    const [formData, setFormData] = useState<Supplier>({
        id: '',
        name: "",
        description: "",
        websiteUrl: "",
        costs: 0,
        deliveryDate: "",
        assignedTasks: [],
        contactEmail: "",
        contactPhone: "",
        contactAddress: ""
    });


    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    }


    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        createSupplier(formData).then(onSave);
    };

    return (
        <form onSubmit={handleSubmit} className="add-supplier-form">
            <h3>Add new Supplier</h3>
            <SupplierFormFields formData={formData} handleChange={handleChange}/>
            <button type="submit">Hinzufügen</button>
        </form>
    );
}