import React, {useState} from 'react';
import {Guest, rsvpStatuses} from "../FrontendSchema.ts";
import {createGuest} from "../../api/GuestService.ts";
import {GuestFormFields} from "./GuestFormFields.tsx";


interface AddGuestFormProps {
    onGuestAdded: (newGuest: Guest) => void;
}

export default function AddGuestForm({ onGuestAdded }: Readonly<AddGuestFormProps>) {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        rsvpStatus: rsvpStatuses[0].value,
        notes: ""
    });


    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
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
                    notes: ''
                });
            })
            .catch(error => console.error('Error adding guest:', error));
    };

    return (
        <form onSubmit={handleSubmit} className="add-guest-form">
            <h3>Neuen Gast hinzufügen</h3>
            <GuestFormFields formData={formData} handleChange={handleChange}/>
            <button type="submit">Hinzufügen</button>
        </form>
    );
}