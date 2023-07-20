import { apiAuthorizedPost, apiGet } from "../api/api";
import { CREATE_PASSWORD, GET_PASSWORDS } from "../api/urls";

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
};

export default PasswordService;
