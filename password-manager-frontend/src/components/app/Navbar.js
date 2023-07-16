import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import MenuIcon from "@mui/icons-material/Menu";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import AdbIcon from "@mui/icons-material/Adb";
import { LockOpen } from "@mui/icons-material";
import { RouteNames } from "../../routes/RouteNames";
import { ButtonGroup } from "@mui/material";
import { connect } from "react-redux";
import { Logout } from "../../services/AuthService";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

function ResponsiveAppBar(props) {
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const user = props.user;
	const pages = [
		{
			key: 1,
			name: "Home",
			route: RouteNames.HOME,
		},
		{
			key: 2,
			name: "Contact",
			route: RouteNames.CONTACT,
		},
		...(user
			? [
					{
						key: 3,
						name: "Profile",
						route: RouteNames.PROFILE,
					},
					{
						key: 4,
						name: "Manager",
						route: RouteNames.MANAGER,
					},
			  ]
			: []),
	];

	const [anchorElNav, setAnchorElNav] = React.useState(null);

	const handleOpenNavMenu = (event) => {
		setAnchorElNav(event.currentTarget);
	};

	const handleCloseNavMenu = () => {
		setAnchorElNav(null);
	};

	return (
		<AppBar position="sticky">
			<Container maxWidth="xl" spacing={1}>
				<Toolbar disableGutters>
					<LockOpen sx={{ display: { xs: "none", md: "flex" }, mr: 1 }} />
					<Typography
						variant="h6"
						noWrap
						component="a"
						href="/"
						sx={{
							mr: 2,
							display: { xs: "none", md: "flex" },
							fontFamily: "monospace",
							fontWeight: 800,
							letterSpacing: ".1rem",
							color: "inherit",
							textDecoration: "none",
						}}
					>
						PASSWORD MANAGER
					</Typography>

					<Box
						sx={{
							flexGrow: 1,
							display: { xs: "flex", md: "none" },
						}}
					>
						<IconButton
							size="large"
							aria-label="account of current user"
							aria-controls="menu-appbar"
							aria-haspopup="true"
							onClick={handleOpenNavMenu}
							color="inherit"
						>
							<MenuIcon />
						</IconButton>
						<Menu
							id="menu-appbar"
							anchorEl={anchorElNav}
							anchorOrigin={{
								vertical: "bottom",
								horizontal: "left",
							}}
							keepMounted
							transformOrigin={{
								vertical: "top",
								horizontal: "left",
							}}
							open={Boolean(anchorElNav)}
							onClose={handleCloseNavMenu}
							sx={{
								display: { xs: "block", md: "none" },
							}}
						>
							{pages.map((page) => (
								<Button key={page.key} href={page.route}>
									{page.name}
								</Button>
							))}
						</Menu>
					</Box>
					<AdbIcon sx={{ display: { xs: "flex", md: "none" }, mr: 1 }} />
					<Typography
						variant="h5"
						noWrap
						component="a"
						href=""
						sx={{
							mr: 2,
							display: { xs: "flex", md: "none" },
							flexGrow: 1,
							fontFamily: "monospace",
							fontWeight: 700,
							letterSpacing: ".3rem",
							color: "inherit",
							textDecoration: "none",
						}}
					></Typography>
					<Box
						sx={{
							flexGrow: 1,
							display: { xs: "none", md: "flex" },
						}}
					>
						{pages.map((page) => (
							<Button
								key={page.key}
								href={page.route}
								sx={{ my: 2, color: "white", display: "block" }}
							>
								{page.name}
							</Button>
						))}
					</Box>

					<Box sx={{ flexGrow: 0 }}>
						{user ? (
							<ButtonGroup
								variant="contained"
								color="secondary"
								aria-label="outlined button group"
							>
								<Button onClick={() => Logout(dispatch, navigate)}>
									Logout
								</Button>
							</ButtonGroup>
						) : (
							<ButtonGroup
								variant="contained"
								color="secondary"
								aria-label="outlined button group"
							>
								<Button href={RouteNames.REGISTER}>Sign up</Button>
								<Button href={RouteNames.LOGIN}>Sign in</Button>
							</ButtonGroup>
						)}
					</Box>
				</Toolbar>
			</Container>
		</AppBar>
	);
}

function mapStateToProps(state) {
	const { isLoggedIn, user } = state.auth;
	return {
		isLoggedIn,
		user,
	};
}
export default connect(mapStateToProps)(ResponsiveAppBar);
