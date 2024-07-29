import './App.css'
import {Route, Routes} from "react-router-dom";
import GuestList from "./GuestList.tsx";
import GuestDetail from "./GuestDetail.tsx";
import TaskManager from "./TaskManager.tsx";
import HomePage from "./HomePage.tsx";
import TaskDetail from "./TaskDetail.tsx";

function App() {


  return (
    <>
      <div className="App">
      <Routes>
          <Route path="/" element={<HomePage/>}/>
          <Route path="/guests" element={<GuestList/>}/>
          <Route path="/guests/:id" element={<GuestDetail/>}/>
          <Route path="/tasks" element={<TaskManager/>}/>
          <Route path="/tasks/:id" element={<TaskDetail/>}/>
        </Routes>
      </div>
    </>
  )
}

export default App
