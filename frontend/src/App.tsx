import './App.css'
import {Route, Routes} from "react-router-dom";
import GuestList from "./GuestList.tsx";
import GuestDetail from "./GuestDetail.tsx";

function App() {


  return (
    <>
      <div className="App">
      <Routes>
          <Route path="/" element={<GuestList/>}/>
          <Route path="/guests/:id" element={<GuestDetail/>}/>
        </Routes>
      </div>
    </>
  )
}

export default App
