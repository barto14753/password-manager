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

const Transition = React.forwardRef(function Transition(props, ref) {
	return <Slide direction="up" ref={ref} {...props} />;
});

function PasswordRow(props) {
	const data = props.data;
	const [open, setOpen] = React.useState(false);
	const [showPassword, setShowPassword] = React.useState(false);
	const handleOpen = () => setOpen(true);
	const handleClose = () => setOpen(false);

	const handleVisibilityToggle = () => {
		setShowPassword(!showPassword);
	};

	return (
		<TableRow key={data.id} onClick={handleOpen} hover={true}>
			<TableCell>{data.name}</TableCell>
			<TableCell>{milisecondsToDate(data.created)}</TableCell>
			<TableCell>{milisecondsToDate(data.modified)}</TableCell>
			<TableCell>{data.versions}</TableCell>
			<Dialog
				fullScreen
				open={open}
				TransitionComponent={Transition}
				keepMounted
				onClose={handleClose}
			>
				<DialogTitle>{data.name}</DialogTitle>
				<DialogContent dividers>
					<DialogContentText>
						Created: {milisecondsToDate(data.created)}
					</DialogContentText>
					<DialogContentText>
						Modified: {milisecondsToDate(data.modified)}
					</DialogContentText>
					<DialogContentText>Versions: {data.versions}</DialogContentText>
				</DialogContent>

				<TextField
					disabled
					type={showPassword ? "text" : "password"}
					value={data.value}
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
					<Button onClick={handleClose}>Update</Button>
				</DialogActions>
			</Dialog>
		</TableRow>
	);
}

export default PasswordRow;
