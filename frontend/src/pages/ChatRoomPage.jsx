import { useParams } from "react-router";
import BottomBar from "../components/roomin/BottomBar";
import VideoGrid from "../components/roomin/VideoGrid";
import { useCallback, useEffect, useId, useRef, useState } from "react";
import { enterRoom, joinRoom } from "../apis/room";
import RoomHeader from "../components/roomin/RoomHeader";
import { useSelector } from "react-redux";
import useWebRTC from "../hooks/useWebRTC";
import { connectWebSocket } from "../sockets/signaling";

export default function ChatRoomPage() {
    const { roomId } = useParams();

    const user = useSelector((state) => state.auth.user);
    const userId = user?.userId;
    const nickname = user?.nickname;

    const [roomTitle, setRoomTitle] = useState("");

    const [participants, setParticipants] = useState([]);

    const [isConnected, setIsConnected] = useState(false);

    const {
        startLocalStream,
        createAndSendOffer,
        handleSignalMessage,
        onToggleMic,
        onToggleCam,
        onShareScreen,
        leaveRoom,
        isMicOn,
        isCamOn,
        isScreenSharing,
    } = useWebRTC(userId, roomId);

    const memoizedHandler = useCallback(handleSignalMessage, [userId, roomId]);

    // WebSocket 메세지 핸들러는 useCallback으로 고정
    const handleSignal = useCallback(
        (msg) => {
            handleSignalMessage(msg);
        },
        [handleSignalMessage]
    )

    // 최초 입장 시 WebSocket 연결 시도
    useEffect(() => {
        if (!userId || !roomId) return;

        connectWebSocket(roomId, handleSignal, async () => {
            setIsConnected(true) // WebSocket 연결 완료
        });
    }, [userId, roomId, memoizedHandler]);

    const hasEnteredRoom = useRef(false);

    // Websocket 연결 이후에만 enterRoom 실행
    useEffect(() => {
        if (!isConnected || !userId || !roomId ||hasEnteredRoom.current) return;

        const enter = async () => {
            try {
                const streamReady = await startLocalStream();

                if (!streamReady) {
                    console.warn("로컬 스트림이 준비되지 않아 offer 생략");
                    return;
                }

                const res = await enterRoom(roomId);
                const { userId: myId, participants: otherUserIds } = res.data.data;

                // 참가자 상태 갱신
                const newParticipants = [
                    { id: myId, nickname, isMe: true },
                    ...otherUserIds.map((id) => ({
                        id,
                        nickname: `참가자 ${id}`,
                        isMe: false,
                    })),
                ];

                console.log("👥 participants", newParticipants);
                setParticipants(newParticipants);

                // 연결된 유저에게 offer 전송
                otherUserIds.forEach((targetId) => {
                    // 너무 빠르면 실패 가능성 있음, 약간 딜레이
                    setTimeout(() => createAndSendOffer(targetId), 300);
                });

                hasEnteredRoom.current = true;
            } catch (err) {
                console.log("enterRoom 실패", err);
            }
        };

        enter();
    }, [isConnected, userId, roomId]);

    // 내 캠 실행
    useEffect(() => {
        if (!userId) return;

        const inteval = setInterval(() => {
            const videoEl = document.getElementById(`video-${userId}`);
            if (videoEl) {
                startLocalStream();
                clearInterval(inteval);
            } else {
                console.warn("video 태그가 아직 준비되지 않았음");
            }
        }, 200);

        return () => clearInterval(inteval);
    }, [userId, startLocalStream]);
    

    useEffect(() => {
        if (!userId || !roomId) return;

        console.log("roomId: ", roomId);
        const join = async () => {
            try {
                const res = await joinRoom(roomId);
                setRoomTitle(res.data.data.roomTitle);
            } catch (err) {
                console.error("방 가입 또는 재가입 실패: ", err);
                alert("방 가입에 실패했습니다.");
            }
        }

        if (userId) {
            join();
        }
    }, [roomId, userId])

    return (
        <>
            <RoomHeader roomId={roomId} roomTitle={roomTitle} />
            <VideoGrid users={participants}/>
            <BottomBar 
                onToggleMic={onToggleMic}
                onToggleCam={onToggleCam}
                onShareScreen={onShareScreen}
                isMicOn={isMicOn}
                isCamOn={isCamOn}
                isScreenSharing={isScreenSharing}
                leaveRoom={leaveRoom}
                roomId={roomId}
            />
        </>
    )
}