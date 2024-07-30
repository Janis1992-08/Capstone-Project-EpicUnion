import axios from 'axios';
import {Guest, GuestForm} from "../components/FrontendSchema.ts";


const API_URL = '/api/guests';

export const getGuests = () => axios.get<Guest[]>(API_URL);
export const getGuestById = (id: string) => axios.get<Guest>(`${API_URL}/${id}`);
export const createGuest = (guest: GuestForm) => axios.post<Guest>(API_URL, guest);
export const updateGuest = (id: string, guest: GuestForm) => axios.put<Guest>(`${API_URL}/${id}`, guest);
export const deleteGuest = (id: string) => axios.delete<void>(`${API_URL}/${id}`);
export const assignTaskToGuest = (guestId: string, taskId: string) => axios.put<void>(`${API_URL}/${guestId}/tasks/${taskId}`);
export const removeTaskFromGuest = (guestId: string, taskId: string) => axios.put<void>(`${API_URL}/${guestId}/tasks/remove/${taskId}`);
