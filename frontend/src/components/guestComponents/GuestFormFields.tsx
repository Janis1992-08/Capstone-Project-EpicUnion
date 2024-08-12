import React from 'react';
import {Guest, rsvpStatuses, Task} from "../FrontendSchema.ts";

interface GuestFormFieldsProps {
    formData: Guest;
    tasks: Task[];
    handleAssignedToTask: (event: React.ChangeEvent<HTMLInputElement>) => void;
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
}

export function GuestFormFields({ formData, handleChange , tasks, handleAssignedToTask}: GuestFormFieldsProps) {
    return (
        <>
            <div>
                <label htmlFor="firstName">First Name:</label>
                <input
                    type="text"
                    id="firstName"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="lastName">Last Name:</label>
                <input
                    type="text"
                    id="lastName"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="email">Email:</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor={"phone"}>Phone:</label>
                <input
                    type="tel"
                    id="phone"
                    name="phone"
                    value={formData.phoneNumber}
                    onChange={handleChange}

                />
            </div>
            <div>
                <label htmlFor="rsvpStatus">RSVP Status:</label>
                <select
                    id="rsvpStatus"
                    name="rsvpStatus"
                    value={formData.rsvpStatus}
                    onChange={handleChange}
                    required
                >
                    {rsvpStatuses.map(rsvpStatus => (
                        <option key={rsvpStatus.value} value={rsvpStatus.value}>{rsvpStatus.label}</option>
                    ))}
                </select>
            </div>
            <div>
                <label htmlFor="notes">Notizen:</label>
                <textarea
                    id="notes"
                    name="notes"
                    value={formData.notes}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="handleAssignedToTask">Assigned Tasks:</label>
                {tasks.map(task => (
                    <div key={task.id}>
                        <label>
                            <input
                                type="checkbox"
                                name="handleAssignedToTask"
                                value={task.id}
                                checked={formData.assignedTasks.includes(task.id)}
                                onChange={handleAssignedToTask}
                            />
                            {task.title}
                        </label>
                    </div>
                ))}
            </div>
        </>
    );
}
