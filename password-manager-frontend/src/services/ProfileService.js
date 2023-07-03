import { apiGet, apiPatch } from "../api/api";
import { GET_PROFILE, PATCH_PROFILE, } from "../api/urls";


const ProfileService = {
    get(dispatch) {
        const body = {}
        return apiGet(GET_PROFILE, body)
            .then(response => {
                    localStorage.setItem('user', JSON.stringify(response['profile']));
                }
            )
    },

    update(dispatch, newFirstName, newLastName) {
        const body = {
            firstName: newFirstName,
            lastName: newLastName
          }
        return apiPatch(PATCH_PROFILE, body)
            .then(response => {
                    localStorage.setItem('user', JSON.stringify(response['profile']));
                }
            )
    },

}

export default ProfileService;
