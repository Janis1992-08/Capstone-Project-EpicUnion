import React, {useState} from 'react';
import {Guest, Task, taskStatuses} from "../FrontendSchema.ts";
import {createTask} from "../../api/TaskService.ts";
import {TaskFormFields} from "./TaskFormFields.tsx";


interface AddTaskFormProps {
    onSave: () => void;
    guests: Guest[];
}

export default function AddTaskForm({ onSave, guests }: AddTaskFormProps) {
    const [task, setTask] = useState<Task>({
        id: '',
        title: '',
        description: '',
        dueDate: '',
        taskStatuses: taskStatuses[0].value,
        assignedTo: []
    });

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;
        setTask({ ...task, [name]: value });
    };

    const handleAssignedToChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedOptions = Array.from(event.target.selectedOptions, option => option.value);
        console.log('Selected options:', selectedOptions); // Debugging-Zwecke
        setTask({ ...task, assignedTo: selectedOptions });
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        createTask(task).then(onSave);
    };

    return (
        <form onSubmit={handleSubmit}>
            <TaskFormFields
                task={task}
                guests={guests}
                handleChange={handleChange}
                handleAssignedToChange={handleAssignedToChange}
            />
            <button type="submit">Save</button>
        </form>

    );
}
