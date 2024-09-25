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
import KdbxService from "../../services/KdbxService";
import KdbxCreationDialog from "./KdbxCreationDialog";
import KdbxRow from "./KdbxRow";

function KdbxManager(props) {
	const [kdbxFiles, setKdbxFiles] = useState(null);
	const [alert, setAlert] = useState({
		title: "",
		message: "",
		severity: "",
	});

	const fetchData = async () => {
		try {
			const response = await KdbxService.getFiles();
			setKdbxFiles(response);
		} catch (error) {
			setAlert({
				title: "Error",
				message: "Error fetching data",
				severity: "error",
			});
			console.log("Error fetching data:", error);
		}
	};

	useEffect(() => {
		fetchData();
	}, []);

	const clearAlert = () => {
		setAlert({ title: "", message: "", severity: "" });
	};

	const handleFileCreated = (name) => {
		setAlert({
			title: "File created",
			message: `File ${name} created successfully`,
			severity: "success",
		});
		fetchData();
	};

	const deleteFile = (file) => {
		setAlert({
			title: "File deleted",
			message: "File " + file.name + " deleted",
			severity: "success",
		});
		setKdbxFiles((prevFiles) => prevFiles.filter((f) => f.id !== file.id));
	};

	if (kdbxFiles === null) {
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
								KDBX Files
							</Typography>
							<Table size="small">
								<TableHead>
									<TableRow>
										<TableCell>Id</TableCell>
										<TableCell>Name</TableCell>
										<TableCell>Password</TableCell>
										<TableCell>Open</TableCell>
										<TableCell>Created</TableCell>
									</TableRow>
								</TableHead>
								<TableBody>
									{kdbxFiles.map((file) => (
										<KdbxRow
											key={file.id}
											id={file.id}
											name={file.name}
											created={file.created}
											open={file.open}
											password={file.password}
											onDelete={() => deleteFile(file)}
										/>
									))}
								</TableBody>
							</Table>
						</Paper>
					</Grid>
				</Grid>
				<KdbxCreationDialog onFileCreated={handleFileCreated} />
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

export default connect(mapStateToProps)(KdbxManager);
