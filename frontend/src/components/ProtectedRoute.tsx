import {Navigate, Outlet} from "react-router-dom";


type ProtectedRouteProps = {
    user:string
}
export default function ProtectedRoute(props:Readonly<ProtectedRouteProps>){

    const user = props.user !== undefined && props.user !== "anonymousUser";

    return(
        user ? <Outlet/> : <Navigate to={"/login"}/>
    )
}