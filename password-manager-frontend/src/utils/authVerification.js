import React, { useEffect } from "react";
import { parseJwt } from "./jwtParser";
import { connect, useDispatch } from "react-redux";
import { LOGIN_SUCCESS } from "../reducers/types";
import { apiRefresh } from "../api/api";
import { Logout } from "../services/AuthService";
import { useNavigate } from "react-router-dom";

function AuthVerification(props) {
	const dispatch = useDispatch();
	const navigate = useNavigate();

	useEffect(() => {
		const refresh = () => {
			apiRefresh()
				.then((response) => {
					localStorage.setItem("user", JSON.stringify(response["user"]));
					sessionStorage.setItem("access_token", response["accessToken"]);
					sessionStorage.setItem("refresh_token", response["refreshToken"]);
					dispatch({ type: LOGIN_SUCCESS, payload: response });
				})
				.catch((err) => {
					Logout(dispatch, navigate);
				});
		};

		const access_token = sessionStorage.getItem("access_token");

		if (access_token) {
			const decoded_access_token = parseJwt(access_token);
			const ttl = decoded_access_token.exp * 1000 - Date.now();
			if (ttl < 0) {
				refresh();
			} else {
				setTimeout(() => {
					refresh();
				}, ttl);
			}
		}
	}, [dispatch, navigate, props]);

	return <div>{props.children}</div>;
}

function mapStateToProps(state) {
	const { isLoggedIn, user } = state.auth;
	return {
		isLoggedIn,
		user,
	};
}
export default connect(mapStateToProps)(AuthVerification);
