import axios from 'axios';
import {Guest} from "../components/FrontendSchema.ts";

const API_URL = '/api/guests';

export const getGuests = () => axios.get<Guest[]>(API_URL);
export const getGuestById = (id: string) => axios.get<Guest>(`${API_URL}/${id}`);
export const createGuest = (guest: Guest) => axios.post<Guest>(API_URL, guest);
export const updateGuest = (id: string, guest: Guest) => axios.put<Guest>(`${API_URL}/${id}`, guest);
export const deleteGuest = (id: string) => axios.delete<void>(`${API_URL}/${id}`);