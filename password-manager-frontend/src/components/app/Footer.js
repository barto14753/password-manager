import React from "react";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import Tooltip from "@mui/material/Tooltip";

function Copyright() {
	return (
		<Tooltip title={`<Typography variant="body1" color="textSecondary">`} arrow>
			<Typography variant="body1" color="textSecondary" align="center" mb={1}>
				{"Copyright © "}
				<Link color="inherit" href="https://material-ui.com/">
					Password Manager
				</Link>{" "}
				{new Date().getFullYear()}
				{"."}
			</Typography>
		</Tooltip>
	);
}

export default function Footer(props) {
	return (
		<footer>
			<Container maxWidth="lg">
				<Copyright />
			</Container>
		</footer>
	);
}
