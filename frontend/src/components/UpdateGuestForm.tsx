import React, {useState} from "react";
import axios from "axios";
import {Guest, rsvpStatuses} from "./FrontendSchema.ts";
import { useParams} from "react-router-dom";

interface UpdateGuestFormProps {
    guest: Guest;
    onGuestUpdate: () => void;
}

export default function UpdateGuestForm({guest, onGuestUpdate}: Readonly<UpdateGuestFormProps>) {
    const {id} = useParams<{ id: string }>();
    const [formData, setFormData] = useState({
        name: guest.name,
        email: guest.email,
        rsvpStatus: guest.rsvpStatus,
        notes: guest.notes
    });

    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) {
        setFormData({
            ...formData,
            [event.target.name]: event.target.value
        });
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();

        axios.put(`/api/guests/${id}`, formData)
            .then(response => {
            console.log('Guest updated:', response.data);
                onGuestUpdate();
        })
            .catch(error => console.error(error));

    }

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
                <label htmlFor="notes">Notizen:</label>
                <textarea
                    id="notes"
                    name="notes"
                    value={formData.notes}
                    onChange={handleChange}
                />
            </div>
            <button type="submit">Save</button>
        </form>
    );


}