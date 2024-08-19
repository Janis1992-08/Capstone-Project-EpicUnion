import React, {useEffect, useState} from "react";
import {Guest, Task} from "../FrontendSchema.ts";
import {updateGuest} from "../../api/GuestService.ts";
import {GuestFormFields} from "./GuestFormFields.tsx";
import "../../styling/globals/FormFields.css"

interface UpdateGuestFormProps {
    guest: Guest;
    tasks: Task[];
    onSave: () => void;
}

export default function UpdateGuestForm({ guest, onSave, tasks }: UpdateGuestFormProps) {
    const [formData, setFormData] = useState<Guest>(guest);


    useEffect(() => {
        setFormData(guest);
    }, [guest]);

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
        updateGuest(guest.id, formData).then(onSave);
    };


    return (
        <form onSubmit={handleSubmit} className="update-guest-form">
            <h3>Update Guest</h3>
            <GuestFormFields
                formData={formData}
                handleChange={handleChange}
                tasks={tasks}
                handleAssignedToTask={handleAssignedToTask}
            />
            <button className="form-submit-button" type="submit">Update</button>
        </form>
    );
}