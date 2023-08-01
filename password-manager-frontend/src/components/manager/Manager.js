import React, { useState, useEffect } from "react";
import { Content } from "../app/Content";
import { connect } from "react-redux";
import {
	Unstable_Grid2 as Grid,
	Alert,
	AlertTitle,
	Table,
	TableHead,
	TableRow,
	TableCell,
	TableBody,
	Container,
	Typography,
} from "@mui/material";
import Paper from "@mui/material/Paper";
import PasswordRow from "./PasswordRow";
import PasswordService from "../../services/PasswordService";
import PasswordCreationDialog from "./PasswordCreationDialog";

function Manager(props) {
	const [passwords, setPasswords] = useState(null);

	useEffect(() => {
		const fetchData = async () => {
			try {
				const response = await PasswordService.getPasswords();
				setPasswords(response.passwords);
			} catch (error) {
				console.log("Error fetching data:", error);
			}
		};

		fetchData();
	}, []);

	const [alert, setAlert] = useState({
		title: "",
		message: "",
		severity: "",
	});

	const clearAlert = () => {
		setAlert({ title: "", message: "", severity: "" });
	};

	const deletePassword = (password) => {
		setAlert({
			title: "Password deleted",
			message: "Password " + password.name + " deleted",
			severity: "success",
		});
		setPasswords((prevPasswords) =>
			prevPasswords.filter((pass) => pass.id !== password.id)
		);
	};

	if (passwords === null) {
		return <div>Loading...</div>;
	}

	return (
		<Content>
			{alert.message && (
				<Alert onClose={() => clearAlert()} severity={alert.severity}>
					<AlertTitle>{alert.title}</AlertTitle>
					{alert.message}
				</Alert>
			)}

			<Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
				<Grid container spacing={3}>
					<Grid item xs={12}>
						<Paper
							sx={{
								p: 2,
								display: "flex",
								flexDirection: "column",
							}}
						>
							<Typography
								component="h1"
								variant="h4"
								color="primary"
								gutterBottom
							>
								Passwords
							</Typography>
							<Table size="small">
								<TableHead>
									<TableRow>
										<TableCell>Name</TableCell>
										<TableCell>Created</TableCell>
										<TableCell>Modified</TableCell>
										<TableCell>Versions</TableCell>
									</TableRow>
								</TableHead>
								<TableBody>
									{passwords.map((password) => (
										<PasswordRow
											key={password.id}
											password={password}
											deletePassword={deletePassword}
										/>
									))}
								</TableBody>
							</Table>
						</Paper>
					</Grid>
				</Grid>
				<PasswordCreationDialog />
			</Container>
		</Content>
	);
}

function mapStateToProps(state) {
	const { isLoggedIn, user } = state.auth;
	return {
		isLoggedIn,
		user,
	};
}
export default connect(mapStateToProps)(Manager);
