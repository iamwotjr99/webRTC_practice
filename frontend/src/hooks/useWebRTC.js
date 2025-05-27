import { useRef } from "react";
import { sendSignal } from "../sockets/signaling";

export default function useWebRTC(userId, roomId) {
    // 내가 상대방들과 맺는 RTCPeerConnection 객체를 저장
    const peerConnections = useRef({}); // useRef: 컴포넌트가 리렌더링 되어도 유지
    const localStream = useRef(null);

    const startLocalStream = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
            localStream.current = stream;

            // 내 캠 화면 띄우기
            const localVideo = document.getElementById(`video-${userId}`);
            if (localVideo) localVideo.srcObject = stream;
        } catch (err) {
            console.error("로컬 미디오 가져오기 실패: ", err);
        }
    }

    const createPeerConnection = (targetId) => {
        const pc = new RTCPeerConnection();

        // ICE 후보 처리
        pc.onicecandidate = (e) => {
            if (e.candidate) {
                console.log("candidate: ", e.candidate);
                sendSignal(targetId, "CANDIDATE", e.candidate);
            }
        };

        // 트랙 수신 처리
        // 상대방의 스트림이 내 브라우저에 전달될 때 실행
        pc.ontrack = (e) => {
            console.log("수신된 스트림: ", e.streams);
            // console.log("수신된 스트림: ", e.streams[0]);
            const remoteStream = e.streams[0];

            // TODO: UI에 연결
            const remoteVideo = document.getElementById(`video-${targetId}`);
            if (remoteVideo) remoteVideo.srcObject = remoteStream;
        };

        if (localStream.current) {
            localStream.current.getTracks().forEach((track) => {
                // 내 스트림을 상대방에게 전송
                pc.addTrack(track, localStream.current);
            });
        };

        peerConnections.current[targetId] = pc;
        return pc;
    };

    const createAndSendOffer = async (targetId) => {
        // 상대방과 연결할 RTCPeerConnection 생성
        const pc = createPeerConnection(targetId);

        // 상대방에게 제안할 연결 정보(sdp)를 생성, 내 쪽(local) 설정에 저장
        const offer = await pc.createOffer();
        await pc.setLocalDescription(offer);

        // offer 정보(sdp)를 상대방에게 전송
        sendSignal(targetId, "OFFER", offer);
    };

    // 상대방으로부터 signaling 메시지를 받았을 때 실행
    const handleSignalMessage = async (msg) => {
        const { senderId, type, data } = msg;
        // 이미 연결된 peerConnection이 있으면 그걸 쓰고, 없으면 새로 만듦
        const pc = peerConnections.current[senderId] || createPeerConnection(senderId);

        if (type === "OFFER") {
            // 상대가 보낸 offer를 내 RTCPeerConnection에 반영
            await pc.setRemoteDescription(new RTCSessionDescription(data));

            // 내 응답 answer를 만들고 내 쪽(local) 설정 저장
            const answer = await pc.createAnswer();
            await pc.setLocalDescription(answer);

            // answer 정보(sdp)를 상대방에게 전송
            sendSignal(senderId, "ANSWER", answer);
        } else if (type === "ANSWER") {
            // 내가 offer를 보냈고, 상대가 보낸 answer를 수신한 경우
            // 상대가 보낸 answer를 내 RTCPeerConnection에 반영
            // 내 peer에 적용하여 연결 완료
            await pc.setRemoteDescription(new RTCSessionDescription(data));
        } else if (type === "CANDIDATE") {
            // 상대방이 보내온 네트워크 후보(candidate)를 내 peer에 등록
            await pc.addIceCandidate(new RTCIceCandidate(data));
        }
    };

    return {
        startLocalStream,
        createAndSendOffer,
        handleSignalMessage,
    };
}
