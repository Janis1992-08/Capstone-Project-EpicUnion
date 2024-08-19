import React, {useState} from 'react';
import {Guest, Supplier, Task, taskStatuses} from "../FrontendSchema.ts";
import {createTask} from "../../api/TaskService.ts";
import {TaskFormFields} from "./TaskFormFields.tsx";
import "../../styling/globals/FormFields.css"


interface AddTaskFormProps {
    onSave: () => void;
    guests: Guest[];
    suppliers: Supplier[];
}

export default function AddTaskForm({ onSave, guests, suppliers }: AddTaskFormProps) {
    const [task, setTask] = useState<Task>({
        id: '',
        title: '',
        description: '',
        dueDate: '',
        taskStatus: taskStatuses[0].value,
        assignedToGuests: [],
        assignedToSuppliers: []

    });

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
        createTask(task).then(onSave);
    };

    return (
        <form onSubmit={handleSubmit}>
            <h3>Create new Task</h3>
            <TaskFormFields
                task={task}
                guests={guests}
                suppliers={suppliers}
                handleChange={handleChange}
                handleAssignedToGuests={handleAssignedToGuests}
                handleAssignedToSuppliers={handleAssignedToSuppliers}
            />
            <button className="form-submit-button" type="submit">Add Task</button>
        </form>

    );
}
