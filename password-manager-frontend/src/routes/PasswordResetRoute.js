import PasswordReset from "../components/authentication/PasswordReset";
import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";

export default function PasswordResetRoute() {
    return (
      <Routes>
        <Route
          path={''}
          element={
            <PasswordReset />
          }
        />
  
        <Route path='*' element={<PageNotFound />} />
      </Routes>
    )
  }