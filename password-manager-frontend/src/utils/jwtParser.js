export const parseJwt = (jwt) => {
	try {
		return JSON.parse(atob(jwt.split(".")[1]));
	} catch (e) {
		console.log(e);
	}
};

export const isUser = (access_token) =>
	access_token ? parseJwt(access_token) : false;
