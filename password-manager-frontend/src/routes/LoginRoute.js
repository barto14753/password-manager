import Login from "../components/authentication/Login";
import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";


export default function LoginRoute() {
    return (
      <Routes>
        <Route
          path={''}
          element={
            <Login />
          }
        />
  
        <Route path='*' element={<PageNotFound />} />
      </Routes>
    )
  }