import axios from 'axios';
import {Task} from "../components/FrontendSchema.ts";

const API_URL = '/api/tasks';

export const getTasks = () => axios.get<Task[]>(API_URL);
export const getTaskById = (id: string) => axios.get<Task>(`${API_URL}/${id}`);
export const createTask = (task: Task) => axios.post<Task>(API_URL, task);
export const updateTask = (id: string, task: Task) => axios.put<Task>(`${API_URL}/${id}`, task);
export const deleteTask = (id: string) => axios.delete<void>(`${API_URL}/${id}`);
