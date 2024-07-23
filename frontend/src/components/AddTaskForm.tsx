import React, {useEffect, useState} from 'react';
import { Task, taskStatuses} from "./FrontendSchema.ts";
import {createTask, updateTask} from "../api/TaskService.ts";


interface TaskFormProps {
    initialTask?: Task | null;
    onSave: () => void;
}

export default function AddTaskForm({ initialTask, onSave }: Readonly<TaskFormProps>) {
    const [task, setTask] = useState<Task>(initialTask || {
        id: '',
        title: '',
        description: '',
        dueDate: '',
        taskStatuses: '',
        assignedTo: ''
    });

    useEffect(() => {
        if (initialTask) {
            setTask(initialTask);
        }
    }, [initialTask]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = event.target;
        setTask({ ...task, [name]: value });
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        if (task.id) {
            updateTask(task.id, task).then(onSave);
        } else {
            createTask(task).then(onSave);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="title">Title:</label>
                <input
                    type="text"
                    id="title"
                    name="title"
                    value={task.title}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="description">Description:</label>
                <textarea
                    id="description"
                    name="description"
                    value={task.description}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="dueDate">Due Date:</label>
                <input
                    type="date"
                    id="dueDate"
                    name="dueDate"
                    value={task.dueDate}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="taskStatuses">Task Status:</label>
                <select
                    id="taskStatuses"
                    name="taskStatuses"
                    value={task.taskStatuses}
                    onChange={handleChange}
                    required
                >
                    {taskStatuses.map(taskStatuses => (
                        <option key={taskStatuses.value} value={taskStatuses.value}>{taskStatuses.label}</option>
                    ))}
                </select>
            </div>
            <div>
                <label htmlFor="assignedTo">Assigned To:</label>
                <input
                    type="text"
                    id="assignedTo"
                    name="assignedTo"
                    value={task.assignedTo}
                    onChange={handleChange}
                />
            </div>
            <button type="submit">Save</button>
        </form>
    );
}



