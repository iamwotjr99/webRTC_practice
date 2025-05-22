import axios from "axios";

export const loginApi = (nickname, password) => {
    return axios.post("/api/auth/login", {nickname: nickname, password: password});
};

export const registerApi = (nickname, password) => {
    return axios.post("/api/auth/register", {nickname: nickname, password: password});
};