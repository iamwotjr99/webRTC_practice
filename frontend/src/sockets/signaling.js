import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { leaveRoomApi } from "../apis/room";

let stompClient = null;

const BASE_URL = "http://localhost:8080";

export const connectWebSocket = (roomId, onSignalReceived, onConnected, onParticipantUpdate, onUserLeave) => {
    const accessToken = localStorage.getItem("accessToken");

    // 이미 연결돼있으면 중복 연결 방지
    if (stompClient && stompClient.connected) {
        console.log("⚠️ 이미 WebSocket 연결되어 있음");
        return;
    }

    stompClient = new Client({
        brokerURL: `ws://20.41.106.213:8080/ws-stomp?accessToken=${accessToken}`,
        connectHeaders: {
            Authorization: `Bearer ${accessToken}`,
        },
        onConnect: () => {
            // 시그널 수신
            stompClient.subscribe(`/sub/signal/${roomId}`, (msg) => {
                console.log("msg: ", msg);
                const signal = JSON.parse(msg.body);
                onSignalReceived(signal);
            })

            // 실시간 참여자 목록 수신
            stompClient.subscribe(`/sub/room/${roomId}/participants`, (msg) => {
                const participants = JSON.parse(msg.body);
                console.log("참여자 업데이트: ", participants);
                if (onParticipantUpdate) onParticipantUpdate(participants);
            })

            // 나간 유저 id 수신
            stompClient.subscribe(`/sub/room/${roomId}/leave`, (msg) => {
                const leaverId = JSON.parse(msg.body);
                console.log("유저 퇴장: ", leaverId);
                if (onUserLeave) onUserLeave(leaverId);
            });

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

export const publishParticipantUpdate = (roomId) => {
    if (!stompClient || !stompClient.connected) {
        console.warn("Websocket 연결이 안되어있음");
        return;
    }

    stompClient.publish({
        destination: `/pub/room/${roomId}/participants`,
        body: "",
    });
}

export const disconnectWebSocket = async (roomId) => {
    if (stompClient) {
        await leaveRoomApi(roomId);

        // 방 나갈 때 participant 업데이트를 위한 나가는 유저 id 브로드캐스트
        stompClient.publish({
            destination: `/pub/room/${roomId}/leave`,
            body: "",
        })

        stompClient.deactivate();
        stompClient = null;

        console.log("WebSocket 연결 종료");
    }
}