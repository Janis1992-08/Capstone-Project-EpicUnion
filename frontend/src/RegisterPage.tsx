import {FormEvent, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import "./styling/RegisterPage.css"


export default function RegisterPage() {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        email: ""
    });
    const [errors, setErrors] = useState({
        username: "",
        password: "",
        email: ""
    });
    const [successMessage, setSuccessMessage] = useState("");
    const [isModalVisible, setIsModalVisible] = useState(false);
    const nav = useNavigate();

    const validateForm = () => {
        let isValid = true;
        const newErrors = {
            username: "",
            password: "",
            email: ""
        };


        if (formData.username.length < 4) {
            newErrors.username = "Username must be at least 4 characters.";
            isValid = false;
        }


        const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$&*])[A-Za-z\d!@#$&*]{4,}$/;
        if (!passwordRegex.test(formData.password)) {
            newErrors.password = "Password must be at least 4 characters, include one uppercase letter and one special character.";
            isValid = false;
        }


        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(formData.email)) {
            newErrors.email = "Please enter a valid email.";
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    function submitRegister(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();
        if (!validateForm()) return;
        axios.post("/api/user/register", formData)
            .then(() => {
                setSuccessMessage(`Welcome to Epic Union, ${formData.username}!`);
                setIsModalVisible(true);
                setTimeout(() => {
                    setIsModalVisible(false);
                    nav("/login");
                }, 5000);
            })
            .catch(error => {
                console.error('Registration failed:', error);
            });
    }

    return (
        <>
            <div className={`${isModalVisible ? 'register-blur' : ''}`}>
            <h1>Registration Page</h1>
                <form onSubmit={submitRegister} className={`register-form ${isModalVisible ? 'register-blur' : ''}`}>
                    <div className="register-group">
                        <label htmlFor="username" className="register-label">Username:</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={e => setFormData({...formData, username: e.target.value})}
                            placeholder="Please enter your Username"
                            required
                            className="register-input"
                        />
                        {errors.username && <p className="register-error">{errors.username}</p>}
                    </div>
                    <div className="register-group">
                        <label htmlFor="password" className="register-label">Password:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={e => setFormData({...formData, password: e.target.value})}
                            placeholder="Please enter your Password"
                            required
                            className="register-input"
                        />
                        {errors.password && <p className="register-error">{errors.password}</p>}
                    </div>
                    <div className="register-group">
                        <label htmlFor="email" className="register-label">Email:</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={e => setFormData({...formData, email: e.target.value})}
                            placeholder="Please enter your Email"
                            required
                            className="register-input"
                        />
                        {errors.email && <p className="register-error">{errors.email}</p>}
                    </div>
                    <button type="submit" className="register-button">Register</button>
                </form>
            </div>
            {isModalVisible && (
                <div className="register-modal">
                    <div className="register-modal-content">
                        <p className="register-success">{successMessage}</p>
                    </div>
                </div>
            )}
                <Link to="/login" className="register-link">Go to Login Page</Link>
            </>
            );
            }