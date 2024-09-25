import axios from "axios";
import { POST_REFRESH } from "./urls";

export function apiPost(url, body) {
	const config = {
		headers: {
			"Content-Type": "application/json",
		},
	};
	return axios
		.post(url, JSON.stringify(body), config)
		.then((response) => response.data)
		.catch((error) => {
			throw error;
		});
}

export function apiAuthorizedPost(
	url,
	body,
	file = null,
	contentType = "application/json"
) {
	const accessToken = sessionStorage.getItem("access_token");
	const config = {
		headers: {
			Authorization: `Bearer ${accessToken}`,
			withCredentials: true,
		},
	};
	let bodyData;
	if (file) {
		console.log("file", file);
		bodyData = new FormData();
		bodyData.append("file", file, file.name);
		config.headers["Content-Type"] = "multipart/form-data";
	} else {
		config.headers["Content-Type"] = contentType;
		bodyData = contentType === "application/json" ? JSON.stringify(body) : body;
	}

	return axios
		.post(url, bodyData, config)
		.then((response) => response.data)
		.catch((error) => {
			throw error;
		});
}

export function apiGet(url, params) {
	const accessToken = sessionStorage.getItem("access_token");
	const headers = {
		Authorization: `Bearer ${accessToken}`,
	};
	return axios
		.get(url, {
			params: params,
			withCredentials: true,
			headers: headers,
		})
		.then((response) => response.data)
		.catch((error) => {
			throw error;
		});
}

export function apiPatch(url, body) {
	const accessToken = sessionStorage.getItem("access_token");
	const headers = {
		Authorization: `Bearer ${accessToken}`,
	};

	return axios
		.patch(url, body, {
			withCredentials: true,
			headers: headers,
		})
		.then((response) => response.data)
		.catch((error) => {
			throw error;
		});
}

export function apiDelete(url) {
	const accessToken = sessionStorage.getItem("access_token");
	const headers = {
		Authorization: `Bearer ${accessToken}`,
	};
	console.log(url);
	return axios
		.delete(url, {
			withCredentials: true,
			headers: headers,
		})
		.then((response) => response.data)
		.catch((error) => {
			throw error;
		});
}

export function apiRefresh() {
	const refreshToken = sessionStorage.getItem("refresh_token");
	const config = {
		headers: {
			Authorization: `Bearer ${refreshToken}`,
			"Content-Type": "application/json",
			withCredentials: true,
		},
	};
	return axios
		.post(POST_REFRESH, JSON.stringify({}), config)
		.then((response) => response.data)
		.catch((error) => {
			throw error;
		});
}
