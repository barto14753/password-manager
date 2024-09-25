import PageNotFound from "../components/main/PageNotFound";
import { Route, Routes } from "react-router-dom";
import PageGuard from "../components/PageGuard";
import { PageAccessType } from "../utils/pageAccessType";
import KdbxManager from "../components/kdbx/KdbxManager";

export default function KdbxRoute() {
	return (
		<Routes>
			<Route
				path={""}
				element={
					<PageGuard role={PageAccessType.LOGGED_IN}>
						<KdbxManager />
					</PageGuard>
				}
			/>

			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
