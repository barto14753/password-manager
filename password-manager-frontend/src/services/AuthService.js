import { useDispatch } from "react-redux";
import { apiPost } from "../api/api";
import { POST_LOGIN, POST_PASSWORD_RESET, POST_REGISTER } from "../api/urls";
import { LOGIN_SUCCESS, LOGOUT, REGISTER_SUCCESS } from "../reducers/types";

const AuthService = {
    register(email, first_name, last_name, password) {
        const body = {
            email: email,
            first_name: first_name,
            last_name: last_name,
            password: password,
          }
        return apiPost(POST_REGISTER, body)
            .then(response => {
                    localStorage.setItem('user', JSON.stringify(response['user']));
                    sessionStorage.setItem('access_token', response['accessToken']);
                    sessionStorage.setItem('refresh_token', response['refreshToken']);
                    useDispatch()({ type: REGISTER_SUCCESS, payload: response });
                }
            )
    },

    login(email, password) {
        const body = {
            email: email,
            password: password,
          }
        return apiPost(POST_LOGIN, body)
            .then(response => {
                    localStorage.setItem('user', JSON.stringify(response['user']));
                    sessionStorage.setItem('access_token', response['accessToken']);
                    sessionStorage.setItem('refresh_token', response['refreshToken']);
                    useDispatch()({ type: LOGIN_SUCCESS, payload: response });
                }
            )
    },

    resetPassword(email, oldPassword, newPassword) {
        const body = {
            email: email,
            oldPassword: oldPassword,
            newPassword: newPassword
          }
        return apiPost(POST_PASSWORD_RESET, body)
            .then(response => {
                    console.log("Password reset done: " + response);
                }
            )
            .catch(err => {
                throw err
            });
    },
}

export function Logout() {
    sessionStorage.removeItem("access_token");
    sessionStorage.removeItem("refresh_token");
    useDispatch({ type: LOGOUT });
}

export default AuthService;
