

export interface Guest {
    id: string;
    name: string;
    email: string;
    rsvpStatus: string;
    notes: string;
    assignedTasks: string[];
}

export interface Task {
    id: string;
    title: string;
    description: string;
    taskStatus: string;
    dueDate: string;
    assignedToGuests: string[];
    assignedToSuppliers: string[];
}

export interface Supplier {
    id: string;
    name: string;
    description: string;
    websiteUrl: string;
    costs: number;
    deliveryDate: string;
    assignedTasks: string[];
    contactEmail: string;
    contactPhone: string;
    contactAddress: string;
}


export interface GuestForm {
    name: string;
    email: string;
    rsvpStatus: string;
    notes: string;
}

export const rsvpStatuses = [
    { value: "PENDING", label: "Pending" },
    { value: "CONFIRMED", label: "Confirmed" },
    { value: "DECLINED", label: "Declined" },
];

export const taskStatuses = [
    { value: "OPEN", label: "To Do" },
    { value: "IN_PROGRESS", label: "In Progress" },
    { value: "DONE", label: "Done" }
];
