import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { leaveRoomApi } from "../apis/room";

let stompClient = null;

const BASE_URL = "http://localhost:8080";

export const connectWebSocket = (roomId, onSignalReceived, onConnected) => {
    const accessToken = localStorage.getItem("accessToken");

    // 이미 연결돼있으면 중복 연결 방지
    if (stompClient && stompClient.connected) {
        console.log("⚠️ 이미 WebSocket 연결되어 있음");
        return;
    }

    stompClient = new Client({
        brokerURL: `ws://localhost:8080/ws-stomp?accessToken=${accessToken}`,
        connectHeaders: {
            Authorization: `Bearer ${accessToken}`, // 선택
        },
        onConnect: () => {
            stompClient.subscribe(`/sub/signal/${roomId}`, (msg) => {
                console.log("msg: ", msg);
                const signal = JSON.parse(msg.body);
                onSignalReceived(signal);
            })
            if (onConnected) onConnected();
        },
        onStompError: (frame) => {
            console.error("STOMP 오류", frame);
        },
        });

    stompClient.activate();
};

export const sendSignal = (roomId, userId, targetId, type, data) => {
    if (!stompClient || !stompClient.connected) {
        console.warn("stompClient가 연결되지 않았습니다.");
        return;
    }

    const payload = {
        senderId: userId,
        targetId,
        signalType : type,
    };

    if (type === "OFFER" || type === "ANSWER") {
        payload.sdp = data.sdp;
    } else if (type === "CANDIDATE") {
        payload.candidate = data.candidate;
        payload.sdpMid = data.sdpMid;
        payload.sdpMLineIndex = data.sdpMLineIndex;
    }

    stompClient.publish({
        destination: `/pub/signal/${roomId}`,
        body: JSON.stringify({...payload}),
    });
};

export const disconnectWebSocket = (roomId) => {
    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
        leaveRoomApi(roomId);
        console.log("WebSocket 연결 종료");
    }
}