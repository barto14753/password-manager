import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";
import Profile from "../components/profile/Profile";

export default function ProfileRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.LOGGED_IN}>
						<Profile />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
