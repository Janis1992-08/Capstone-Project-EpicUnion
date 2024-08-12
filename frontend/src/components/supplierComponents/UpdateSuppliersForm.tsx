import React, {useEffect, useState} from "react";
import { Supplier} from "../FrontendSchema.ts";
import {updateSupplier} from "../../api/SupplierService.ts";
import {SupplierFormFields} from "./SupplierFormFields.tsx";

interface UpdateSupplierFormProps {
    initialSupplier: Supplier;
    onSave: () => void;
}


export default function UpdateSuppliersForm({ initialSupplier, onSave }: UpdateSupplierFormProps) {
    const [formData, setFormData] = useState<Supplier>(initialSupplier);

    useEffect(() => {
        setFormData(initialSupplier);
    }, [initialSupplier]);


    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };



    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        updateSupplier(initialSupplier.id, formData).then(onSave);
    };

    return (
        <form onSubmit={handleSubmit} className="update-guest-form">
            <h3>Update Guest</h3>
            <SupplierFormFields
                formData={formData}
                handleChange={handleChange}
            />
            <button type="submit">Update</button>
        </form>
    );
}