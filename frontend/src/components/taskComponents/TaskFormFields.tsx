import React from 'react';
import {Guest, Task, taskStatuses} from "../FrontendSchema.ts";

interface TaskFormFieldsProps {
    task: Task;
    guests: Guest[];
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
    handleAssignedToChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
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
                <label htmlFor="taskStatus">Task Status:</label>
                <select
                    id="taskStatus"
                    name="taskStatus"
                    value={task.taskStatus}
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
                {guests.map(guest => (
                    <div key={guest.id}>
                        <label>
                            <input
                                type="checkbox"
                                name="assignedTo"
                                value={guest.id}
                                checked={task.assignedTo.includes(guest.id)}
                                onChange={handleAssignedToChange}
                            />
                            {guest.name}
                        </label>
                    </div>
                ))}
            </div>
        </>
    );
}
