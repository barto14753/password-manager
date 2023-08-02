import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";
import Manager from "../components/manager/Manager";

export default function ManagerRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.LOGGED_IN}>
						<Manager />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
