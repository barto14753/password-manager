import {
	LOGIN_FAIL,
	LOGIN_SUCCESS,
	LOGOUT,
	REGISTER_FAIL,
	REGISTER_SUCCESS,
} from "./types";

const user = JSON.parse(localStorage.getItem("user"));
const access_token = sessionStorage.getItem("access_token");
const refresh_token = sessionStorage.getItem("refresh_token");
const initialState = user
	? {
			isLoggedIn: true,
			user,
			access_token: access_token,
			refresh_token: refresh_token,
	  }
	: {
			isLoggedIn: false,
			user: null,
			access_token: access_token,
			refresh_token: refresh_token,
	  };

export default function useAppSelector(state = initialState, action) {
	const { type, payload } = action;
	switch (type) {
		case REGISTER_SUCCESS:
			return {
				...state,
				isLoggedIn: true,
				user: payload.user,
				accessToken: payload.accessToken,
				refreshToken: payload.refreshToken,
			};
		case REGISTER_FAIL:
			return {
				...state,
				isLoggedIn: false,
			};
		case LOGIN_SUCCESS:
			return {
				...state,
				isLoggedIn: true,
				user: payload.user,
				accessToken: payload.accessToken,
				refreshToken: payload.refreshToken,
			};
		case LOGIN_FAIL:
			return {
				...state,
				isLoggedIn: false,
				user: null,
			};
		case LOGOUT:
			return {
				...state,
				isLoggedIn: false,
				user: null,
				accessToken: null,
				refreshToken: null,
			};
		default:
			return state;
	}
}
