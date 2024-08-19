import React, {useState} from 'react';
import {Guest, rsvpStatuses, Task} from "../FrontendSchema.ts";
import {createGuest} from "../../api/GuestService.ts";
import {GuestFormFields} from "./GuestFormFields.tsx";
import "../../styling/globals/FormFields.css"


interface AddGuestFormProps {
    onSave: () => void;
    tasks: Task[];
}

export default function AddGuestForm({ onSave, tasks }: Readonly<AddGuestFormProps>) {
    const [formData, setFormData] = useState<Guest>({
        id: '',
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        rsvpStatus: rsvpStatuses[0].value,
        notes: "",
        assignedTasks: []
    });


    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    const handleAssignedToTask = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value, checked } = event.target;
        setFormData(prevTask => ({
            ...prevTask,
            assignedTasks: checked
                ? [...prevTask.assignedTasks, value]
                : prevTask.assignedTasks.filter(id => id !== value)
        }));
    };


    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        createGuest(formData).then(onSave);
        alert(`Guest '${formData.firstName} ${formData.lastName}' has been added!`);
    };

    return (
        <form onSubmit={handleSubmit} className="add-guest-form">
            <h3>Create new Guest</h3>
            <GuestFormFields formData={formData} handleChange={handleChange} tasks={tasks} handleAssignedToTask={handleAssignedToTask}/>
            <button className="form-submit-button" type="submit">Add Guest</button>
        </form>
    );
}