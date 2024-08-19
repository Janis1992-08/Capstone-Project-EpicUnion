import React from 'react';
import {Guest, Supplier, Task, taskStatuses} from "../FrontendSchema.ts";
import "../../styling/globals/FormFields.css";

interface TaskFormFieldsProps {
    task: Task;
    guests: Guest[];
    suppliers: Supplier[];
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
    handleAssignedToGuests: (event: React.ChangeEvent<HTMLInputElement>) => void;
    handleAssignedToSuppliers: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

export function TaskFormFields({ task, guests, handleChange, handleAssignedToGuests, handleAssignedToSuppliers, suppliers }: TaskFormFieldsProps) {
    return (
        <div className="form-fields">
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
            <div className="checkbox-group">
                <label htmlFor="assignedToGuests">Assigned Guests:</label>
                {guests.map(guest => (
                    <div key={guest.id}>
                        <label>
                            <input
                                type="checkbox"
                                name="assignedToGuests"
                                value={guest.id}
                                checked={task.assignedToGuests.includes(guest.id)}
                                onChange={handleAssignedToGuests}
                            />
                            {guest.firstName}
                        </label>
                    </div>
                ))}
            </div>
            <div className="checkbox-group">
                <label htmlFor="assignedToSuppliers">Assigned Suppliers:</label>
                {suppliers.map(supplier => (
                    <div key={supplier.id}>
                        <label>
                            <input
                                type="checkbox"
                                name="assignedToSuppliers"
                                value={supplier.id}
                                checked={task.assignedToSuppliers.includes(supplier.id)}
                                onChange={handleAssignedToSuppliers}
                            />
                            {supplier.name}
                        </label>
                    </div>
                ))}
            </div>
        </div>
    );
}
