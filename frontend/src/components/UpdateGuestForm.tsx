import React, {useEffect, useState} from "react";
import {Guest, rsvpStatuses, Task} from "./FrontendSchema.ts";
import {updateGuest} from "../api/GuestService.ts";
import {getTasks} from "../api/TaskService.ts";

interface UpdateGuestFormProps {
    guest: Guest;
    onGuestUpdate: () => void;
}

export default function UpdateGuestForm({guest, onGuestUpdate}: Readonly<UpdateGuestFormProps>) {
    const [formData, setFormData] = useState({
        name: guest.name,
        email: guest.email,
        rsvpStatus: guest.rsvpStatus,
        notes: guest.notes,
        taskIds: guest.taskIds
    });

    const [tasks, setTasks] = useState<Task[]>([]);

    useEffect(() => {
        getTasks().then(response => setTasks(response.data));
    }, []);

    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    function handleTaskChange(event: React.ChangeEvent<HTMLSelectElement>) {
        const selectedTaskId = event.target.value;
        setFormData(prevState => {

            if (selectedTaskId === "") return prevState;

            const updatedTaskIds = [...prevState.taskIds];
            if (!updatedTaskIds.includes(selectedTaskId)) {
                updatedTaskIds.push(selectedTaskId);
            }

            return {
                ...prevState,
                taskIds: updatedTaskIds
            };
        });
    }

    function handleTaskRemove(taskId: string) {
        setFormData(prevState => ({
            ...prevState,
            taskIds: prevState.taskIds.filter(id => id !== taskId)
        }));
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        updateGuest(guest.id, formData)
            .then(response => {
                console.log('Guest updated:', response.data);
                onGuestUpdate();
            })
            .catch(error => console.error('Error updating guest:', error));
    };

    return (
        <form onSubmit={handleSubmit} className="update-guest-form">
            <h3>Update Guest</h3>
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
                <label htmlFor="notes">Notes:</label>
                <textarea
                    id="notes"
                    name="notes"
                    value={formData.notes}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="tasks">Assign Task:</label>
                <select
                    id="tasks"
                    name="task"
                    onChange={handleTaskChange}
                >
                    <option value="">Select a task</option>
                    {tasks.map(task => (
                        <option key={task.id} value={task.id}>{task.title}</option>
                    ))}
                </select>
            </div>
            <div>
                <h4>Assigned Tasks:</h4>
                <ul>
                    {formData.taskIds.map(taskId => {
                        const task = tasks.find(t => t.id === taskId);
                        return task ? (
                            <li key={task.id}>
                                {task.title}
                                <button type="button" onClick={() => handleTaskRemove(task.id)}>Remove</button>
                            </li>
                        ) : null;
                    })}
                </ul>
            </div>
            <button type="submit">Update</button>
        </form>
    );
}