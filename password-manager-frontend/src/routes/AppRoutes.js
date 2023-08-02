import { Route, Routes } from "react-router-dom";
import PageNotFound from "../components/main/PageNotFound";
import HomeRoute from "./HomeRoute";
import LoginRoute from "./LoginRoute";
import PasswordResetRoute from "./PasswordResetRoute";
import ProfileRoute from "./ProfileRoutes";
import RegisterRoute from "./RegisterRoute";
import ContactRoute from "./ContactRoute";
import ManagerRoute from "./ManagerRoute";

export default function AppRoutes() {
	return (
		<Routes>
			<Route path={"/home/*"} element={<HomeRoute />} />
			<Route path={"/contact"} element={<ContactRoute />} />
			<Route path={"/"} element={<HomeRoute />} />
			<Route path={"/login/*"} element={<LoginRoute />} />
			<Route path={"/register/*"} element={<RegisterRoute />} />
			<Route path={"/password-reset/*"} element={<PasswordResetRoute />} />
			<Route path={"/profile/*"} element={<ProfileRoute />} />
			<Route path={"/manager/*"} element={<ManagerRoute />} />
			<Route path="*" element={<PageNotFound />} />
		</Routes>
	);
}
