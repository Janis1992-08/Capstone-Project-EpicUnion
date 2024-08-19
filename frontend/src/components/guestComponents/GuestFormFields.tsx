import React, {useState} from 'react';
import {Guest, rsvpStatuses, Task} from "../FrontendSchema.ts";
import "../../styling/globals/FormFields.css";

interface GuestFormFieldsProps {
    formData: Guest;
    tasks: Task[];
    handleAssignedToTask: (event: React.ChangeEvent<HTMLInputElement>) => void;
    handleChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
}

export function GuestFormFields({ formData, handleChange , tasks, handleAssignedToTask}: GuestFormFieldsProps) {
    const [showTasks, setShowTasks] = useState(false);

    const toggleTasks = () => setShowTasks(!showTasks);

    return (
        <div className="form-fields">
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
                <label htmlFor={"phoneNumber"}>Phone:</label>
                <input
                    type="tel"
                    id="phoneNumber"
                    name="phoneNumber"
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
                <button type="button" onClick={toggleTasks} className="toggle-button">
                    {showTasks? 'Hide Assigned Tasks' : 'Show Assigned Tasks'}
                </button>
                {showTasks && (
            <div className="checkbox-group">
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
                )}
            </div>
        </div>
    );
}
