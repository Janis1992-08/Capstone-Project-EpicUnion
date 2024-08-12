import axios from "axios";
import { Supplier} from "../components/FrontendSchema.ts";

const API_URL = '/api/suppliers';

export const getSuppliers = () => axios.get<Supplier[]>(API_URL);
export const getSupplierById = (id: string) => axios.get<Supplier>(`${API_URL}/${id}`);
export const createSupplier = (supplier: Supplier) => axios.post<Supplier>(API_URL, supplier);
export const updateSupplier = (id: string, supplier: Supplier) => axios.put<Supplier>(`${API_URL}/${id}`, supplier);
export const deleteSupplier = (id: string) => axios.delete<void>(`${API_URL}/${id}`);
