import { apiAuthorizedPost, apiDelete, apiGet } from "../api/api";
import { CREATE_PASSWORD, DELETE_PASSWORD, GET_PASSWORDS } from "../api/urls";

const PasswordService = {
	getPasswords(dispatch) {
		return apiGet(GET_PASSWORDS, {});
	},

	createPassword(dispatch, name, password) {
		return apiAuthorizedPost(CREATE_PASSWORD, {
			name: name,
			value: password,
		});
	},

	deletePassword(dispatch, id) {
		const url = DELETE_PASSWORD + "/" + id;
		console.log(url);
		return apiDelete(url);
	},
};

export default PasswordService;
