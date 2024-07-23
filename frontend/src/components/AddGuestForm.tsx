import React, {useState} from "react";
import axios from "axios";
import {rsvpStatuses} from "./FrontendSchema.ts";



interface AddGuestFormProps {
    onGuestAdded: () => void;
}

export default function AddGuestForm({onGuestAdded}: Readonly<AddGuestFormProps>) {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        rsvpStatus: rsvpStatuses[0].value,
        notes: ""
    });

    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) {
        setFormData({
            ...formData,
            [event.target.name]: event.target.value
        });
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();

        axios.post('/api/guests', formData).then(response => {
            console.log('Guest added:', response.data);
            onGuestAdded();
            setFormData({name: '', email: '', rsvpStatus: rsvpStatuses[0].value, notes: ''});
        })
            .catch(error => console.error(error));

    }

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