import {FormEvent, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import"./styling/LoginPage.css";


type LoginPageProps= {
    setUser:(username:string) => void
}
export default function LoginPage({ setUser }: LoginPageProps) {
    const [formData, setFormData] = useState({ username: "", password: "" });
    const nav = useNavigate();

    function submitLogin(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();
        axios.post("/api/user/login", undefined, { auth: { username: formData.username, password: formData.password } })
            .then(response => {
                setUser(response.data);
                nav("/homepage");
            });
    }

    return (
        <>
            <h1>Login Page</h1>
    <div className="login-form">
            <form onSubmit={submitLogin}>
                <div className="login-group">
                    <label htmlFor="username" className="login-label">Username:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={e => setFormData({ ...formData, username: e.target.value })}
                        placeholder="Please enter your Username"
                        className="login-input"
                        required
                    />
                </div>
                <div className="login-group">
                    <label htmlFor="password" className="login-label">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={e => setFormData({ ...formData, password: e.target.value })}
                        placeholder="Please enter your Password"
                        className="login-input"
                        required
                    />
                </div>
                <button className="login-button">Login</button>
            </form>
            <Link to="/" className="login-link">Go to Registration Page</Link>
        </div>
        </>
    );
}