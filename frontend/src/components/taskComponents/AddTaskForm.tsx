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
        taskStatus: taskStatuses[0].value,
        assignedTo: []
    });

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
