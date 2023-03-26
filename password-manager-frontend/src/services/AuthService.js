import { apiPost } from "../api/api";
import { POST_LOGIN, POST_PASSWORD_RESET, POST_REGISTER } from "../api/urls";

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
                    localStorage.setItem('access_token', response['accessToken']);
                    localStorage.setItem('refresh_token', response['refreshToken']);
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
                    localStorage.setItem('access_token', response['accessToken']);
                    localStorage.setItem('refresh_token', response['refreshToken']);
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
    }
}

export default AuthService;
