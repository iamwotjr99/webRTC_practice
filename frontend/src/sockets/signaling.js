import SockJs from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;
let signalHandler = null;

const BASE_URL = "http://localhost:8080";

export const connectWebSocket = (roomId, onSignalReceived) => {
    stompClient = new Client({
        webSocketFactory: () => new SockJs(`${BASE_URL}/ws-stomp`),
        onConnect: () => {
            console.log("WebSocket 연결됨");

            stompClient.subscribe(`/sub/signal/${roomId}`, (msg) => {
                console.log("msg: ", msg);
                const signal = JSON.parse(msg.body);
                onSignalReceived(signal);
            });
        },
    });

    signalHandler = onSignalReceived;
    stompClient.activate();
};

export const sendSignal = (targetId, type, data) => {
    if (!stompClient) return;

    stompClient.send(
        "/pub/signal",
        {},
        JSON.stringify({ receiverId: targetId, type, data })
    );
};

export const disconnectWebSocket = () => {
    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
    }
}