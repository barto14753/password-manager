import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import Register from "../components/authentication/Register";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";

export default function RegisterRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.NOT_LOGGED_IN}>
						<Register />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
