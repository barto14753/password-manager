import { apiAuthorizedPost, apiGet } from "../api/api";
import { CREATE_KDBX, GET_FILES } from "../api/urls";

const KdbxService = {
	getFiles(dispatch) {
		return apiGet(GET_FILES, {});
	},

	createFiles(dispatch, name, file, password) {
		const url = CREATE_KDBX + `?name=${name}&password=${password}`;
		const contentType = "multipart/form-data";
		return apiAuthorizedPost(url, {}, file, contentType);
	},
};

export default KdbxService;
