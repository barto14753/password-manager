import React, { useEffect } from "react";
import { connect } from "react-redux";
import { useNavigate } from "react-router-dom";
import { isUser } from "../utils/jwtParser";
import { PageAccessType } from "../utils/pageAccessType";
import { RouteNames } from "../routes/RouteNames";

function PageGuard(props) {
	const navigate = useNavigate();
	const isLoggedIn = props.isLoggedIn;
	const user = isUser(props.access_token);
	const role = props.role;
	const child = props.children;

	useEffect(() => {
		if (isLoggedIn && role === PageAccessType.NOT_LOGGED_IN) {
			navigate(RouteNames.PROFILE);
		} else if (!isLoggedIn && role === PageAccessType.LOGGED_IN) {
			navigate(RouteNames.LOGIN);
		}
	}, [navigate, role, isLoggedIn, user]);

	return <>{child}</>;
}

function mapStateToProps(state) {
	const { isLoggedIn, user, access_token, refresh_token } = state.auth;
	return {
		isLoggedIn,
		user,
		access_token,
		refresh_token,
	};
}
export default connect(mapStateToProps)(PageGuard);
