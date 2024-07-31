import './styling/HomePage.css';

export default function HomePage() {
    return (
        <div className="homepage">
            <h1 className="homepage__title">Welcome to Epic Union</h1>
            <nav className="homepage__nav">
                <ul>
                    <li><a href="/guests">Guests</a></li>
                    <li><a href="/tasks">Tasks</a></li>
                </ul>
            </nav>
        </div>
    );

}