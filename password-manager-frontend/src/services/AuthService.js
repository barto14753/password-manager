import { useDispatch } from "react-redux";
import { apiPost } from "../api/api";
import { POST_LOGIN, POST_PASSWORD_RESET, POST_REGISTER } from "../api/urls";
import { LOGIN_SUCCESS, LOGOUT, REGISTER_SUCCESS } from "../reducers/types";
import { RouteNames } from "../routes/RouteNames";


const AuthService = {
    register(dispatch, email, first_name, last_name, password) {
        const body = {
            email: email,
            firstName: first_name,
            lastName: last_name,
            password: password,
          }
        return apiPost(POST_REGISTER, body)
            .then(response => {
                    localStorage.setItem('user', JSON.stringify(response['user']));
                    sessionStorage.setItem('access_token', response['accessToken']);
                    sessionStorage.setItem('refresh_token', response['refreshToken']);
                    dispatch({ type: REGISTER_SUCCESS, payload: response });
                }
            )
    },

    login(dispatch, email, password) {
        const body = {
            email: email,
            password: password
        }
        return apiPost(POST_LOGIN, body)
            .then(response => {
                    localStorage.setItem('user', JSON.stringify(response['user']));
                    sessionStorage.setItem('access_token', response['accessToken']);
                    sessionStorage.setItem('refresh_token', response['refreshToken']);
                    dispatch({ type: LOGIN_SUCCESS, payload: response });
                }
            )
    },

    resetPassword(dispatch, email, oldPassword, newPassword) {
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

export function Logout(dispatch, navigate) {
    sessionStorage.removeItem("access_token");
    sessionStorage.removeItem("refresh_token");
    localStorage.removeItem("user");
    dispatch({ type: LOGOUT });
    navigate(RouteNames.HOME);
}

export default AuthService;
