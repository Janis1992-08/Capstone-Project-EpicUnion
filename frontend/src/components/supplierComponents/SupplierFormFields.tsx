import React from "react";
import {Supplier, Task} from "../FrontendSchema.ts";


interface SupplierFormFieldsProps {
    supplier: Supplier;
    tasks: Task[];
    handleAssignedToTask: (event: React.ChangeEvent<HTMLInputElement>) => void;
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
}

export function SupplierFormFields({ supplier, handleChange, tasks, handleAssignedToTask }: SupplierFormFieldsProps) {
    return (
        <div className="form-fields">
            <div>
                <label htmlFor="name">Name:</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={supplier.name}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="description">Description:</label>
                <input
                    type="text"
                    id="description"
                    name="description"
                    value={supplier.description}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="websiteUrl">Website URL:</label>
                <input
                    type="text"
                    id="websiteUrl"
                    name="websiteUrl"
                    value={supplier.websiteUrl}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="costs">Costs:</label>
                <input
                    type="number"
                    id="costs"
                    name="costs"
                    value={supplier.costs.toString()}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="deliveryDate">Delivery Date:</label>
                <input
                    type="date"
                    id="deliveryDate"
                    name="deliveryDate"
                    value={supplier.deliveryDate}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="contactEmail">Contact Email:</label>
                <input
                    type="email"
                    id="contactEmail"
                    name="contactEmail"
                    value={supplier.contactEmail}
                    onChange={handleChange}

                />
            </div>
            <div>
                <label htmlFor="contactPhone">Contact Phone:</label>
                <input
                    type="tel"
                    id="contactPhone"
                    name="contactPhone"
                    value={supplier.contactPhone}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="contactAddress">Contact Address:</label>
                <input
                    type="text"
                    id="contactAddress"
                    name="contactAddress"
                    value={supplier.contactAddress}
                    onChange={handleChange}
                />
            </div>
            <div className="checkbox-group">
                <label htmlFor="handleAssignedToTask">Assigned Tasks:</label>
                {tasks.map(task => (
                    <div key={task.id}>
                        <label>
                            <input
                                type="checkbox"
                                name="handleAssignedToTask"
                                value={task.id}
                                checked={supplier.assignedTasks.includes(task.id)}
                                onChange={handleAssignedToTask}
                            />
                            {task.title}
                        </label>
                    </div>
                ))}
            </div>
        </div>
    );
}
