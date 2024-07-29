import React from 'react';
import {Guest, Task, taskStatuses} from "../FrontendSchema.ts";

interface TaskFormFieldsProps {
    task: Task;
    guests: Guest[];
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
    handleAssignedToChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
}

export function TaskFormFields({ task, guests, handleChange, handleAssignedToChange }: TaskFormFieldsProps) {
    return (
        <>
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
                    {taskStatuses.map(taskStatus => (
                        <option key={taskStatus.value} value={taskStatus.value}>{taskStatus.label}</option>
                    ))}
                </select>
            </div>
            <div>
                <label htmlFor="assignedTo">Assigned To:</label>
                <select
                    id="assignedTo"
                    name="assignedTo"
                    multiple
                    value={task.assignedTo}
                    onChange={handleAssignedToChange}
                >
                    {guests.map(guest => (
                        <option key={guest.id} value={guest.id}>{guest.name}</option>
                    ))}
                </select>
            </div>
        </>
    );
}
