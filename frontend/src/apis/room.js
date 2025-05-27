import instance from "./axios"

export const createRoom = (title, capacity) => {
    return instance.post("/api/room/create", { title, capacity });
}

export const getMyRooms = () => {
    return instance.get("/api/room/my");
}

export const getMyRoomsRecent = () => {
    return instance.get("/api/room/my/recent");
}

export const joinRoom = (roomId) => {
    return instance.post(`api/room/join/${roomId}`)
}

export const enterRoom = (roomId) => {
    return instance.post(`/api/room/enter/${roomId}`);
}