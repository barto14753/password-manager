import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import AddIcon from "@mui/icons-material/Add";
import Fab from "@mui/material/Fab";
import PasswordService from "../../services/PasswordService";
import { useDispatch } from "react-redux";

export default function PasswordCreationDialog() {
	const dispatcher = useDispatch();
	const [open, setOpen] = React.useState(false);
	const [formData, setFormData] = React.useState({
		name: "",
		password: "",
	});

	const handleChange = (event) => {
		const { id, value } = event.target;
		setFormData((prevFormData) => ({
			...prevFormData,
			[id]: value,
		}));
	};

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};

	const handleCreate = () => {
		PasswordService.createPassword(
			dispatcher,
			formData.name,
			formData.password
		).then(() => window.location.reload());
		setOpen(false);
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
				Add password
			</Fab>

			<Dialog open={open} onClose={handleClose}>
				<DialogTitle>Add password</DialogTitle>
				<DialogContent>
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
						autoFocus
						margin="dense"
						id="password"
						label="Password"
						value={formData.password}
						type="password"
						fullWidth
						variant="standard"
						onChange={handleChange}
					/>
				</DialogContent>
				<DialogActions>
					<Button onClick={handleClose}>Cancel</Button>
					<Button onClick={handleCreate}>Create</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
}
