import React from "react";
import { Content } from "../app/Content";
import Typography from "@mui/material/Typography";

function PageNotFound() {
	return (
		<Content>
			<Typography
				component="h1"
				variant="h2"
				align="center"
				color="textPrimary"
				gutterBottom
				mt={5}
			>
				404: Page not found
			</Typography>
		</Content>
	);
}

export default PageNotFound;
