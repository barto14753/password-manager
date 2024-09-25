import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import AddIcon from "@mui/icons-material/Add";
import Fab from "@mui/material/Fab";
import { useDispatch } from "react-redux";
import IconButton from "@mui/material/IconButton";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import InputAdornment from "@mui/material/InputAdornment";
import KdbxService from "../../services/KdbxService";

export default function KdbxCreationDialog(props) {
	const dispatcher = useDispatch();
	const [open, setOpen] = React.useState(false);
	const [formData, setFormData] = React.useState({
		name: "",
		password: "",
	});
	const [showPassword, setShowPassword] = React.useState(false);
	const [kdbxFile, setKdbxFile] = React.useState(null);
	const onFileCreated = props.onFileCreated;

	const handleChange = (event) => {
		const { id, value } = event.target;
		setFormData((prevFormData) => ({
			...prevFormData,
			[id]: value,
		}));
	};

	const handleFileChange = (event) => {
		const file = event.target.files[0];
		if (file && file.name.endsWith(".kdbx")) {
			setKdbxFile(file);
			setFormData((prevFormData) => ({
				...prevFormData,
				name: file.name.replace(".kdbx", ""),
			}));
		} else {
			alert("Please select a valid .kdbx file");
			event.target.value = null;
		}
	};

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
		setKdbxFile(null);
	};

	const handleCreate = () => {
		KdbxService.createFiles(
			dispatcher,
			formData.name,
			kdbxFile,
			formData.password
		);
		onFileCreated(formData.name);
		handleClose();
	};

	const handleClickShowPassword = () => {
		setShowPassword(!showPassword);
	};

	return (
		<div>
			<Fab
				color="secondary"
				variant="extended"
				onClick={handleClickOpen}
				style={{ position: "fixed", bottom: "20px", right: "20px" }}
			>
				<AddIcon sx={{ mr: 1 }} />
				Add file
			</Fab>

			<Dialog open={open} onClose={handleClose}>
				<DialogTitle>Add KDBX File</DialogTitle>
				<DialogContent>
					<input
						accept=".kdbx"
						style={{ display: "none" }}
						id="raised-button-file"
						type="file"
						onChange={handleFileChange}
					/>
					<label htmlFor="raised-button-file">
						<Button variant="contained" component="span">
							Choose KDBX File
						</Button>
					</label>
					{kdbxFile && <p>Selected file: {kdbxFile.name}</p>}
					<TextField
						autoFocus
						margin="dense"
						id="name"
						label="Name"
						value={formData.name}
						fullWidth
						variant="standard"
						onChange={handleChange}
					/>
					<TextField
						margin="dense"
						id="password"
						label="Password"
						value={formData.password}
						type={showPassword ? "text" : "password"}
						fullWidth
						variant="standard"
						onChange={handleChange}
						InputProps={{
							endAdornment: (
								<InputAdornment position="end">
									<IconButton
										aria-label="toggle password visibility"
										onClick={handleClickShowPassword}
										edge="end"
									>
										{showPassword ? <VisibilityOff /> : <Visibility />}
									</IconButton>
								</InputAdornment>
							),
						}}
					/>
				</DialogContent>
				<DialogActions>
					<Button onClick={handleClose}>Cancel</Button>
					<Button onClick={handleCreate} disabled={!kdbxFile}>
						Create
					</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
}
