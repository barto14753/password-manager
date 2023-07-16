import Login from "../components/authentication/Login";
import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";

export default function LoginRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.NOT_LOGGED_IN}>
						<Login />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
