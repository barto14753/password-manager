import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import Register from "../components/authentication/Register";


export default function RegisterRoute() {
    return (
      <Routes>
        <Route
          path={''}
          element={
            <Register />
          }
        />
  
        <Route path='*' element={<PageNotFound />} />
      </Routes>
    )
  }