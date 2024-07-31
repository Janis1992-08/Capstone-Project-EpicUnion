

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
    assignedTo: string[];
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
