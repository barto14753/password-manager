import { apiPost } from "../api/api";
import { POST_LOGIN, POST_REGISTER } from "../api/urls";

const AuthService = {
    register(email, first_name, last_name, password) {
        const body = {
            email: email,
            first_name: first_name,
            last_name: last_name,
            password: password,
          }
        console.log(body);
        apiPost(POST_REGISTER, body)
            .then(response => {
                    localStorage.setItem('access_token', response['accessToken']);
                    localStorage.setItem('refresh_token', response['refreshToken']);
                }
            )
            .catch(err => console.log(err));
    },

    login(email, password) {
        console.log(email)
        const body = {
            email: email,
            password: password,
          }
        console.log(body);
        apiPost(POST_LOGIN, body)
            .then(response => {
                    localStorage.setItem('access_token', response['accessToken']);
                    localStorage.setItem('refresh_token', response['refreshToken']);
                }
            )
            .catch(err => console.log(err));
    }
}

export default AuthService;
