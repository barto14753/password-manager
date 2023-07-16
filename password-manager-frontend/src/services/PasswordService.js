import { apiGet } from "../api/api";
import { GET_PASSWORDS } from "../api/urls";

const PasswordService = {
	getPasswords(dispatch) {
		return apiGet(GET_PASSWORDS, {});
	},
};

export default PasswordService;
