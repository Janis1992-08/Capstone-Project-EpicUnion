import {Supplier, Task} from "../FrontendSchema.ts";
import React, {useState} from "react";
import {createSupplier} from "../../api/SupplierService.ts";
import {SupplierFormFields} from "./SupplierFormFields.tsx";
import "../../styling/globals/FormFields.css"

interface AddSupplierFormProps {
    onSave: () => void;
    tasks: Task[];
}


export default function AddSupplierForm({ onSave, tasks }: Readonly<AddSupplierFormProps>) {
    const [supplier, setSupplier] = useState<Supplier>({
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
        setSupplier(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

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
        createSupplier(supplier).then(onSave);
    };

    return (
        <form onSubmit={handleSubmit} className="add-supplier-form">
            <h3>Create new Supplier</h3>
            <SupplierFormFields supplier={supplier} handleChange={handleChange} handleAssignedToTask={handleAssignedToTask} tasks={tasks}/>
            <button className="form-submit-button" type="submit">Add Supplier</button>
        </form>
    );
}