

export interface Guest {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
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


export const rsvpStatuses = [
    { value: "PENDING", label: "Pending" },
    { value: "CONFIRMED", label: "Confirmed" },
    { value: "DECLINED", label: "Declined" },
];

export const getRsvpStatusLabel = (statusCode: string) => {
    return rsvpStatuses.find(status => status.value === statusCode)?.label;
}

export const taskStatuses = [
    { value: "OPEN", label: "To Do" },
    { value: "IN_PROGRESS", label: "In Progress" },
    { value: "DONE", label: "Done" }
];

export const getTaskStatusLabel = (statusCode: string) => {
    return taskStatuses.find(status => status.value === statusCode)?.label;
}

export const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('de-DE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};

export const formatCurrency = (amount: number): string => {
    return amount.toLocaleString('de-DE', {
        style: 'currency',
        currency: 'EUR'
    });
};
