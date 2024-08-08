import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";


type LoginPageProps= {
    setUser:(username:string) => void
}
export default function LoginPage(props:Readonly<LoginPageProps>){

    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const nav = useNavigate();

    function submitLogin(e:FormEvent<HTMLFormElement>){
        e.preventDefault()
        axios.post("/api/user/login", undefined, {auth: {username, password}})
            .then(r => props.setUser(r.data))
            .then(() => nav("/homepage"))
    }

    return(
        <form onSubmit={submitLogin}>
            <input value={username} placeholder={"Please enter your Username"} type={"text"}
                   onChange={e => setUsername(e.target.value)}/>
            <input value={password} placeholder={"Please enter your Password"} type={"password"}
                   onChange={e => setPassword(e.target.value)}/>
            <button>Login</button>
        </form>
    )
}