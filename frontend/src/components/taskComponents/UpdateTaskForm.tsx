import React, { useState, useEffect } from 'react';
import {Guest, Supplier, Task} from "../FrontendSchema.ts";
import {updateTask} from "../../api/TaskService.ts";
import {TaskFormFields} from "./TaskFormFields.tsx";
import "../../styling/globals/FormFields.css"

interface UpdateTaskFormProps {
    initialTask: Task;
    onSave: () => void;
    guests: Guest[];
    suppliers: Supplier[];
}

export default function UpdateTaskForm({ initialTask, onSave, guests, suppliers }: UpdateTaskFormProps) {
    const [task, setTask] = useState<Task>(initialTask);

    useEffect(() => {
        setTask(initialTask);
    }, [initialTask]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;
        setTask({ ...task, [name]: value });
    };

    const handleAssignedToGuests = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value, checked } = event.target;
        setTask(prevTask => ({
            ...prevTask,
            assignedToGuests: checked
                ? [...prevTask.assignedToGuests, value]
                : prevTask.assignedToGuests.filter(id => id !== value)
        }));
    };

    const handleAssignedToSuppliers = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value, checked } = event.target;
        setTask(prevTask => ({
            ...prevTask,
            assignedToSuppliers: checked
                ? [...prevTask.assignedToSuppliers, value]
                : prevTask.assignedToSuppliers.filter(id => id !== value)
        }));
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        updateTask(task.id, task).then(onSave);
    };

    return (
        <form onSubmit={handleSubmit}>
            <h3>Update Task</h3>
            <TaskFormFields
                task={task}
                guests={guests}
                suppliers={suppliers}
                handleChange={handleChange}
                handleAssignedToGuests={handleAssignedToGuests}
                handleAssignedToSuppliers={handleAssignedToSuppliers}
            />
            <button className="form-submit-button" type="submit">Update</button>
        </form>
    );
}
