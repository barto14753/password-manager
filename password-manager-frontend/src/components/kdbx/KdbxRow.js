import React from "react";
import { TableRow, TableCell, Dialog, Slide } from "@mui/material";
import { milisecondsToDate } from "../../utils/dateParser";
import { useDispatch } from "react-redux";

const Transition = React.forwardRef(function Transition(props, ref) {
	return <Slide direction="up" ref={ref} {...props} />;
});

function KdbxRow(props) {
	const id = props.id;
	const name = props.name;
	const created = parseInt(props.created);
	const isOpen = props.open.toString();
	const password = props.password;
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

	const handleVisibilityToggle = () => {
		setShowPassword(!showPassword);
	};

	return (
		<TableRow key={id} onClick={handleOpen} hover={true}>
			<TableCell>{id}</TableCell>
			<TableCell>{name}</TableCell>
			<TableCell>{password}</TableCell>
			<TableCell>{isOpen}</TableCell>
			<TableCell>{milisecondsToDate(created)}</TableCell>
			<Dialog></Dialog>
		</TableRow>
	);
}

export default KdbxRow;
