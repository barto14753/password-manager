import React from "react";
import {
	TableRow,
	TableCell,
	Button,
	DialogTitle,
	DialogContent,
	DialogContentText,
	DialogActions,
	Dialog,
	Slide,
	IconButton,
	InputAdornment,
	TextField,
} from "@mui/material";
import { milisecondsToDate } from "../../utils/dateParser";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useDispatch } from "react-redux";
import PasswordService from "../../services/PasswordService";

const Transition = React.forwardRef(function Transition(props, ref) {
	return <Slide direction="up" ref={ref} {...props} />;
});

function PasswordRow(props) {
	const password = props.password;
	const deletePassword = props.deletePassword;
	const dispatcher = useDispatch();

	const [open, setOpen] = React.useState(false);
	const [showPassword, setShowPassword] = React.useState(false);
	const handleOpen = () => {
		setOpen(true);
	};

	const handleClose = (event) => {
		event.stopPropagation();
		setOpen(false);
	};

	const handleDelete = (event) => {
		event.stopPropagation();
		PasswordService.deletePassword(dispatcher, password.id).then(() => {
			deletePassword(password);
			setOpen(false);
		});
	};

	const handleVisibilityToggle = () => {
		setShowPassword(!showPassword);
	};

	return (
		<TableRow key={password.id} onClick={handleOpen} hover={true}>
			<TableCell>{password.name}</TableCell>
			<TableCell>{milisecondsToDate(password.created)}</TableCell>
			<TableCell>{milisecondsToDate(password.modified)}</TableCell>
			<TableCell>{password.versions}</TableCell>
			<Dialog
				fullScreen
				open={open}
				TransitionComponent={Transition}
				keepMounted
				onClose={handleClose}
			>
				<DialogTitle>{password.name}</DialogTitle>
				<DialogContent dividers>
					<DialogContentText>
						Created: {milisecondsToDate(password.created)}
					</DialogContentText>
					<DialogContentText>
						Modified: {milisecondsToDate(password.modified)}
					</DialogContentText>
					<DialogContentText>Versions: {password.versions}</DialogContentText>
				</DialogContent>

				<TextField
					disabled
					type={showPassword ? "text" : "password"}
					value={password.decryptedValue}
					InputProps={{
						endAdornment: (
							<InputAdornment position="end">
								<IconButton onClick={handleVisibilityToggle} edge="end">
									{showPassword ? <Visibility /> : <VisibilityOff />}
								</IconButton>
							</InputAdornment>
						),
					}}
				/>

				<DialogActions>
					<Button color="primary" variant="contained" onClick={handleClose}>
						Update
					</Button>
					<Button color="error" variant="contained" onClick={handleDelete}>
						Delete
					</Button>
				</DialogActions>
			</Dialog>
		</TableRow>
	);
}

export default PasswordRow;
