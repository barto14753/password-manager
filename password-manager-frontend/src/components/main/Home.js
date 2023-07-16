import React from "react";
import { Content } from "../app/Content";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import { CardActionArea } from "@mui/material";
import { RouteNames } from "../../routes/RouteNames";

function Home() {
	const cards = [
		{
			title: "Secure passwords",
			description: "Deciede how secure your password will be",
		},
		{
			title: "Accessible from anywhere",
			description: "Use your phone to get your passwords",
		},
	];
	return (
		<Content>
			<div>
				<Grid mt={5}>
					<Typography
						component="h1"
						variant="h2"
						align="center"
						color="textPrimary"
						gutterBottom
					>
						Password management made easy
					</Typography>
					<Typography
						variant="h6"
						align="center"
						color="textSecondary"
						paragraph
					>
						Purpose of this app is to make Your password management safe, easy
						and accessible from anywhere!
					</Typography>
					<div>
						<Grid container spacing={16} justifyContent="center">
							<Grid item>
								<Button
									variant="contained"
									color="primary"
									size="large"
									href={RouteNames.REGISTER}
								>
									Register
								</Button>
							</Grid>
							<Grid item>
								<Button
									variant="outlined"
									color="primary"
									size="large"
									href={RouteNames.LOGIN}
								>
									Login
								</Button>
							</Grid>
						</Grid>
					</div>
				</Grid>
			</div>
			<div>
				{/* End hero unit */}
				<Grid container spacing={20} justifyContent="center" p={0} mt={0}>
					{cards.map((card) => (
						<Grid item key={card}>
							<Card style={{ width: "400px" }} color="secondary">
								<CardActionArea>
									<CardContent>
										<Typography gutterBottom variant="h5" component="h2">
											{card.title}
										</Typography>
										<Typography>{card.description}</Typography>
									</CardContent>
								</CardActionArea>

								<CardActions>
									<Button size="small" color="primary">
										View
									</Button>
									<Button size="small" color="primary">
										Edit
									</Button>
								</CardActions>
							</Card>
						</Grid>
					))}
				</Grid>
			</div>
		</Content>
	);
}

export default Home;
