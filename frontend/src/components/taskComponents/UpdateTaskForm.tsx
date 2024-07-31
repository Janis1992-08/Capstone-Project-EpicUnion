import React, { useState, useEffect } from 'react';
import {Guest, Task} from "../FrontendSchema.ts";
import {updateTask} from "../../api/TaskService.ts";
import {TaskFormFields} from "./TaskFormFields.tsx";

interface UpdateTaskFormProps {
    initialTask: Task;
    onSave: () => void;
    guests: Guest[];
}

export default function UpdateTaskForm({ initialTask, onSave, guests }: UpdateTaskFormProps) {
    const [task, setTask] = useState<Task>(initialTask);

    useEffect(() => {
        setTask(initialTask);
    }, [initialTask]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;
        setTask({ ...task, [name]: value });
    };

    const handleAssignedToChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value, checked } = event.target;
        setTask(prevTask => ({
            ...prevTask,
            assignedTo: checked
                ? [...prevTask.assignedTo, value]
                : prevTask.assignedTo.filter(id => id !== value)
        }));
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        updateTask(task.id, task).then(onSave);
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
