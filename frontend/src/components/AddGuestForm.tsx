import React, {useEffect, useState} from 'react';
import {Guest, rsvpStatuses, Task} from "./FrontendSchema.ts";
import {createGuest} from "../api/GuestService.ts";
import {getTasks} from "../api/TaskService.ts";


interface AddGuestFormProps {
    onGuestAdded: (newGuest: Guest) => void;
}

export default function AddGuestForm({ onGuestAdded }: Readonly<AddGuestFormProps>) {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        rsvpStatus: rsvpStatuses[0].value,
        notes: "",
        taskIds: [] as string[]
    });

    const [tasks, setTasks] = useState<Task[]>([]);

    useEffect(() => {
        getTasks().then(response => setTasks(response.data))
            .catch(error => console.error('Error fetching tasks:', error));
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

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        createGuest(formData)
            .then(response => {
                console.log('Guest added:', response.data);
                onGuestAdded(response.data);
                setFormData({
                    name: '',
                    email: '',
                    rsvpStatus: rsvpStatuses[0].value,
                    notes: '',
                    taskIds: []
                });
            })
            .catch(error => console.error('Error adding guest:', error));
    };

    return (
        <form onSubmit={handleSubmit} className="add-guest-form">
            <h3>Neuen Gast hinzufügen</h3>
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
                <label htmlFor="taskIds">Assign Task:</label>
                <select
                    id="taskIds"
                    name="taskIds"
                    onChange={handleTaskChange}
                >
                    <option value="">Select a task</option>
                    {tasks.map(task => (
                        <option key={task.id} value={task.id}>{task.title}</option>
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
            <button type="submit">Hinzufügen</button>
        </form>
    );
}