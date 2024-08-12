import React from "react";


interface SupplierFormFieldsProps {
    formData: {
        name: string;
        description: string;
        websiteUrl: string;
        costs: number;
        deliveryDate: string;
        assignedTasks: string[];
        contactEmail: string,
        contactPhone: string,
        contactAddress: string
    };
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
}

export function SupplierFormFields({ formData, handleChange }: SupplierFormFieldsProps) {
    return (
        <>
            <div>
                <label htmlFor="name">Name:</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
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
                    value={formData.description}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="websiteUrl">Website URL:</label>
                <input
                    type="text"
                    id="websiteUrl"
                    name="websiteUrl"
                    value={formData.websiteUrl}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="costs">Costs:</label>
                <input
                    type="number"
                    id="costs"
                    name="costs"
                    value={formData.costs.toString()}
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
                    value={formData.deliveryDate}
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
                    value={formData.contactEmail}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="contactPhone">Contact Phone:</label>
                <input
                    type="tel"
                    id="contactPhone"
                    name="contactPhone"
                    value={formData.contactPhone}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="contactAddress">Contact Address:</label>
                <input
                    type="text"
                    id="contactAddress"
                    name="contactAddress"
                    value={formData.contactAddress}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="assignedTasks">Assigned Tasks:</label>
                <select
                    id="assignedTasks"
                    name="assignedTasks"
                    multiple
                    value={formData.assignedTasks}
                    onChange={handleChange}
                >
                    {/* Dynamically populate tasks here */}
                </select>
            </div>
        </>
    );
}
