import { Route, Routes } from "react-router-dom";
import PageNotFound from "../components/main/PageNotFound";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";
import Contact from "../components/main/Contact";


export default function ContactRoute() {
    return (
      <Routes>
        <Route
          path={''}
          element={
            <PageGuard role={PageAccessType.ALL}>
              <Contact />
            </PageGuard>
          }
        />
  
        <Route path='*' element={<PageNotFound />} />
      </Routes>
    )
  }