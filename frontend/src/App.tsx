import './App.css'
import {Route, Routes} from "react-router-dom";
import GuestList from "./GuestList.tsx";
import GuestDetail from "./GuestDetail.tsx";
import TaskManager from "./TaskManager.tsx";
import HomePage from "./HomePage.tsx";

function App() {


  return (
    <>
      <div className="App">
      <Routes>
            <Route path="/" element={<HomePage/>}/>
          <Route path="/guests" element={<GuestList/>}/>
          <Route path="/tasks" element={<TaskManager/>}/>
          <Route path="/guests/:id" element={<GuestDetail/>}/>
        </Routes>
      </div>
    </>
  )
}

export default App
