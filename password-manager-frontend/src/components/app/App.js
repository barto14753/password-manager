import "../app/App.css";
import Navbar from "./Navbar";
import { ThemeProvider } from "@mui/material/styles";
import { theme } from "../../ThemeOptions";
import { BrowserRouter } from "react-router-dom";
import AppRoutes from "../../routes/AppRoutes";
import Footer from "./Footer";
import AuthVerification from "../../utils/authVerification";

function App() {
	return (
		<ThemeProvider theme={theme}>
			<BrowserRouter>
				<AuthVerification>
					<Navbar />
					<AppRoutes />
					<Footer />
				</AuthVerification>
			</BrowserRouter>
		</ThemeProvider>
	);
}

export default App;
