import React, { useState} from "react";
import {Guest} from "../FrontendSchema.ts";
import {updateGuest} from "../../api/GuestService.ts";
import {GuestFormFields} from "./GuestFormFields.tsx";

interface UpdateGuestFormProps {
    guest: Guest;
    onGuestUpdate: () => void;
}

export default function UpdateGuestForm({ guest, onGuestUpdate }: UpdateGuestFormProps) {
    const [formData, setFormData] = useState({
        name: guest.name,
        email: guest.email,
        rsvpStatus: guest.rsvpStatus,
        notes: guest.notes,
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
            <GuestFormFields
                formData={formData}
                handleChange={handleChange}
            />
            <button type="submit">Update</button>
        </form>
    );
}