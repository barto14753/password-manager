import { Route, Routes } from "react-router-dom";
import Home from "../components/main/Home";
import PageNotFound from "../components/main/PageNotFound";


export default function HomeRoute() {
    return (
      <Routes>
        <Route
          path={''}
          element={
            <Home />
          }
        />
  
        <Route path='*' element={<PageNotFound />} />
      </Routes>
    )
  }