export const BASE_URL = "http://localhost:8080/api";

// AUTHENTICATION
export const POST_LOGIN = BASE_URL + "/auth/login";
export const POST_REGISTER = BASE_URL + "/auth/register";
export const POST_PASSWORD_RESET = BASE_URL + "/auth/password-reset";

// PROFILE
export const GET_PROFILE = BASE_URL + "/profile";
export const PATCH_PROFILE = BASE_URL + "/profile";

// PASSWORD
export const GET_PASSWORDS = BASE_URL + "/password/all";
export const CREATE_PASSWORD = BASE_URL + "/password";
export const DELETE_PASSWORD = BASE_URL + "/password";
