import React, {useEffect, useState} from "react";
import {Supplier, Task} from "../FrontendSchema.ts";
import {updateSupplier} from "../../api/SupplierService.ts";
import {SupplierFormFields} from "./SupplierFormFields.tsx";
import "../../styling/globals/FormFields.css"

interface UpdateSupplierFormProps {
    initialSupplier: Supplier;
    tasks: Task[];
    onSave: () => void;
}


export default function UpdateSuppliersForm({ initialSupplier, onSave, tasks }: UpdateSupplierFormProps) {
    const [supplier, setSupplier] = useState<Supplier>(initialSupplier);

    useEffect(() => {
        setSupplier(initialSupplier);
    }, [initialSupplier]);


    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;
        setSupplier({ ...supplier, [name]: value });
    };

    const handleAssignedToTask = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value, checked } = event.target;
        setSupplier(prevTask => ({
            ...prevTask,
            assignedTasks: checked
                ? [...prevTask.assignedTasks, value]
                : prevTask.assignedTasks.filter(id => id !== value)
        }));
    };



    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        updateSupplier(initialSupplier.id, supplier).then(onSave);
        alert(`Supplier '${supplier.name}' has been updated!`);
    };

    return (
        <form onSubmit={handleSubmit} className="update-guest-form">
            <h3>Update Supplier</h3>
            <SupplierFormFields
                supplier={supplier}
                handleChange={handleChange}
                tasks={tasks}
                handleAssignedToTask={handleAssignedToTask}
            />
            <button className="form-submit-button" type="submit">Update</button>
        </form>
    );
}