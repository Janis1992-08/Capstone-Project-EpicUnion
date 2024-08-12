import './styling/HomePage.css';
import {Link} from "react-router-dom";

export default function HomePage() {
    return (
        <div className="homepage">
            <h1 className="homepage__title">Welcome to Epic Union</h1>
            <nav className="homepage__nav">
                <ul>
                    <li><Link to="/guests">Guests</Link></li>
                    <li><Link to="/tasks">Tasks</Link></li>
                    <li><Link to="/suppliers">Suppliers</Link></li>
                </ul>
            </nav>
        </div>
    );

}