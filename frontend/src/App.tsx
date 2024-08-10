import './App.css'
import {Route, Routes} from "react-router-dom";
import GuestList from "./GuestList.tsx";
import GuestDetail from "./GuestDetail.tsx";
import TaskList from "./TaskList.tsx";
import HomePage from "./HomePage.tsx";
import TaskDetail from "./TaskDetail.tsx";
import {useEffect, useState} from "react";
import axios from "axios";
import RegisterPage from "./RegisterPage.tsx";
import LoginPage from "./LoginPage.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";

function App() {
    const [user, setUser] = useState<string>("anonymousUser");

    useEffect(() => {
        axios.get("/api/user")
            .then(response => setUser(response.data))
            .catch(() => setUser("anonymousUser"));
    }, []);

    function logout() {
        axios.get("/api/user/logout")
            .then(() => setUser("anonymousUser"));
    }

    return (
        <>
            <div className="App">
                {user !== "anonymousUser" && (
                    <button onClick={logout}>Logout</button>
                )}
                <Routes>
                    <Route path="/" element={<RegisterPage />} />
                    <Route path="/login" element={<LoginPage setUser={setUser} />} />
                    <Route element={<ProtectedRoute user={user} />}>
                        <Route path="/homepage" element={<HomePage />} />
                        <Route path="/guests" element={<GuestList />} />
                        <Route path="/guests/:id" element={<GuestDetail />} />
                        <Route path="/tasks" element={<TaskList />} />
                        <Route path="/tasks/:id" element={<TaskDetail />} />
                    </Route>
                </Routes>
            </div>
        </>
    );
}

export default App;