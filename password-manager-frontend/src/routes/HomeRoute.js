import { Route, Routes } from "react-router-dom";
import Home from "../components/main/Home";
import PageNotFound from "../components/main/PageNotFound";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";

export default function HomeRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.ALL}>
						<Home />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
