import { useParams } from "react-router";
import BottomBar from "../components/roomin/BottomBar";
import VideoGrid from "../components/roomin/VideoGrid";
import { useEffect, useState } from "react";
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

    const {
        startLocalStream,
        createAndSendOffer,
        handleSignalMessage,
    } = useWebRTC(userId, roomId);

    // 시그널링 연결(WebSocket 연결)
    useEffect(() => {
        if (userId && roomId) {
            console.log(userId);
            connectWebSocket(roomId, handleSignalMessage);
        }
    }, [userId, roomId, handleSignalMessage])

    // 내 캠 실행
    useEffect(() => {
        if (!userId) {
            console.warn("⛔️ userId 없음 → 캠 실행 보류");
            return;
        }

        console.log("✅ userId 있음 → 캠 실행");
        startLocalStream();
    }, [userId]);
    

    useEffect(() => {
        if (!userId || !roomId) return;

        console.log("roomId: ", roomId);
        const join = async () => {
            try {
                const res = await joinRoom(roomId);
                console.log("방 가입 또는 재가입 성공");
                setRoomTitle(res.data.data.roomTitle);
            } catch (err) {
                console.error("방 가입 또는 재가입 실패: ", err);
                alert("방 가입에 실패했습니다.");
            }
        }

        const enter = async () => {
            try {
                const res = await enterRoom(roomId);
                console.log("enter Room [userIds]: ", res);
                const { userId: myId, participants: otherUserIds } = res.data.data;
                console.log(res.data.data);

                // TODO: 각 otherParticipanId에게 WebSocket offer 전송 시작
                setParticipants([
                    { id: myId, nickname, isMe: true},
                    ...otherUserIds.map((id) => ({
                        id,
                        nickname: `참가자 ${id}`,
                        isMe: false,
                    })),
                ]);

                otherUserIds.forEach((targetId) => {
                    createAndSendOffer(targetId);
                });
            } catch (err) {
                console.error("enterRoom 실패: ", err);
            }
        }

        if (userId) {
            join();
            enter();
        }
    }, [roomId, userId])

    return (
        <>
            <RoomHeader roomId={roomId} roomTitle={roomTitle} />
            <VideoGrid users={participants}/>
            <BottomBar />
        </>
    )
}