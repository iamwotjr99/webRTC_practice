import instance from "./axios";

export const checkNicknameApi = (nickname) => {
    return instance.get("/api/auth/check-nickname", {
        params: { nickname },
    })
};

export const getUserInfo = () => {
    return instance.get("/api/user/me");
}