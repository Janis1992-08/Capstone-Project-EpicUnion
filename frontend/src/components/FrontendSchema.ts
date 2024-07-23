

export interface Guest {
    id: string;
    name: string;
    email: string;
    rsvpStatus: string;
    notes: string;
}

export const rsvpStatuses = [
    { value: "CONFIRMED", label: "Confirmed" },
    { value: "DECLINED", label: "Declined" },
    { value: "PENDING", label: "Pending" }
];