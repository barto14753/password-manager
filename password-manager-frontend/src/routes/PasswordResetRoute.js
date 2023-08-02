import PasswordReset from "../components/authentication/PasswordReset";
import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";

export default function PasswordResetRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.NOT_LOGGED_IN}>
						<PasswordReset />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
