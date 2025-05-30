import { useRef, useState } from "react";
import { disconnectWebSocket, sendSignal } from "../sockets/signaling";

export default function useWebRTC(userId, roomId) {

    const [isMicOn, setIsMicOn] = useState(false);
    const [isCamOn, setIsCamOn] = useState(false);
    const [isScreenSharing, setIsScreenSharing] = useState(false);

    // 내가 상대방들과 맺는 RTCPeerConnection 객체를 저장
    const peerConnections = useRef({}); // useRef: 컴포넌트가 리렌더링 되어도 유지

    // setRemoteDescription()이 끝난 후 candidate들을 add하기 위한 queue
    const pendingCandidates = useRef({});

    const localStream = useRef(null);
    const cameraStream = useRef(null);

    const startLocalStream = async () => {
        if (localStream.current) return true;
        console.log("[로컬 스트림] 시작 시도");
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ 
                video: true, audio: true 
            });

            console.log("[로컬 스트림] 성공", stream);


            localStream.current = stream;
            cameraStream.current = stream;


            const videoTrack = stream.getVideoTracks()[0];
            const audioTrack = stream.getAudioTracks()[0];


            videoTrack.enabled = false;
            audioTrack.enabled = false;

            // console.log("videoTrack.enabled:", videoTrack.enabled);
            // console.log("audioTrack.enabled:", audioTrack.enabled);

            // 내 캠 화면 띄우기
            const localVideo = document.getElementById(`video-${userId}`);
            if (localVideo) {
                localVideo.srcObject = stream;
            } else {
                console.warn("video 태그가 아직 없음. 500ms 후 재시도");
                setTimeout(() => {
                    const retryVideo = document.getElementById(`video-${userId}`);
                    if (retryVideo) {
                        retryVideo.srcObject = stream;
                    } else {
                        console.error("video 태그를 끝내 찾지 못함");
                    }
                }, 500);
            }

            return true;
        } catch (err) {
            console.error("로컬 미디오 가져오기 실패: ", err);
            return false;
        };
    };

    const onToggleMic = () => {
        const audioTrack = localStream.current?.getAudioTracks()[0];
        if (audioTrack) {
            const newState = !audioTrack.enabled
            audioTrack.enabled = newState;
            setIsMicOn(newState);
        };
    };

    const onToggleCam = () => {
        if (isScreenSharing) {
            alert("화면 공유중에는 캠을 킬 수 없습니다.");
            return;
        };

        const videoTrack = localStream.current?.getVideoTracks()[0];
        if (videoTrack) {
            const newState = !videoTrack.enabled;
            videoTrack.enabled = newState
            setIsCamOn(newState);
        };
    };

    const onShareScreen = async () => {
        if (isCamOn) {
            alert("캠을 키는 동안에는 화면 공유를 할 수 없습니다.");
            return;
        };

        if (isScreenSharing) {
            if (localStream.current) {
                localStream.current.getTracks().forEach((track) => track.stop());
            }

            if (cameraStream.current) {
                localStream.current = cameraStream.current;

                const localVideo = document.getElementById(`video-${userId}`);
                if (localVideo) localVideo.srcObject = cameraStream.current;

                const cameraVideoTrack = cameraStream.current.getVideoTracks()[0];
                cameraVideoTrack.enabled = false;

                Object.values(peerConnections.current).forEach((pc) => {
                    const sender = pc.getSenders().find((s) => s.track && s.track.kind === "video")
                    if (sender) {
                        sender.replaceTrack(cameraVideoTrack);
                    }
                });
            }
            setIsScreenSharing(false);

            return;
        }

        try {
            const screenStream = await navigator.mediaDevices.getDisplayMedia({ video: true, audio: false});

            localStream.current = screenStream;

            const localVideo = document.getElementById(`video-${userId}`);
            if (localVideo) localVideo.srcObject = screenStream;

            Object.values(peerConnections.current).forEach((pc) => {
                const sender = pc.getSenders().find((s) => s.track && s.track.kind === "video");
                if (sender) {
                    sender.replaceTrack(screenStream.getVideoTracks()[0]);
                }
            });

            setIsScreenSharing(true);
        } catch (err) {
            console.error("화면 공유 실패: ", err);
        }
    };

    const createPeerConnection = (targetId) => {
        const pc = new RTCPeerConnection({
            iceServers: [
                {urls: "stun:stun.l.google.com:19302"}
            ]
        });

        // ICE 후보 처리
        pc.onicecandidate = (e) => {
            // console.log("onicecandidate 발생", e);
            if (e.candidate && e.candidate.candidate) {
                const { candidate, sdpMid, sdpMLineIndex } = e.candidate;
                console.log("보내는 ICE candidate: ", { candidate, sdpMid, sdpMLineIndex });

                sendSignal(roomId, userId, targetId, "CANDIDATE", {
                    candidate,
                    sdpMid,
                    sdpMLineIndex,
                });
            } else {
                console.log("무효한 ICE candidate. 전송 생략", e.candidate);
            }
        };

        // 트랙 수신 처리
        // 상대방의 스트림이 내 브라우저에 전달될 때 실행
        pc.ontrack = (e) => {
            // console.log("수신된 스트림: ", e.streams);
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
        } else {
            console.warn("localStream 없음. addTrack 생략됨 (나중에 추가될 수 있음)");
        }

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
        sendSignal(roomId, userId, targetId, "OFFER", { sdp: offer.sdp, type: offer.type });
        // console.log("👉 OFFER 보냄", offer.sdp);
    };

    // 상대방으로부터 signaling 메시지를 받았을 때 실행
    const handleSignalMessage = async (msg) => {
        if(msg.senderId == userId) return;

        // console.log("시그널 수신", msg);

        const { senderId, signalType, sdp, candidate, sdpMid, sdpMLineIndex } = msg;

        // PeerConnection 가져오기 (없으면 생성)
        const pc = peerConnections.current[senderId] || createPeerConnection(senderId);

        try {
            if (signalType === "OFFER") {
                // console.log("👉 OFFER 받음", sdp);
                if (!sdp) {
                    console.warn("유효하지 않은 OFFER 수신: sdp 없음");
                    return;
                }

                // 1. 상대방의 offer를 remote으로 설정
                await pc.setRemoteDescription(new RTCSessionDescription({ type: "offer", sdp }));

                // 2. 나의 answer 생성 후 local로 설정
                const answer = await pc.createAnswer();
                await pc.setLocalDescription(answer);

                if (pendingCandidates.current[senderId]) {
                    for (const ice of pendingCandidates.current[senderId]) {
                        try {
                            await pc.addIceCandidate(ice);
                        } catch (err) {
                            console.error("큐 처리 중 ICE candidate 추가 실패", err);
                        }
                    }
                    pendingCandidates.current[senderId] = [];
                }

                // 3. answer를 상대방에게 전송
                sendSignal(roomId, userId, senderId, "ANSWER", {
                    sdp: answer.sdp,
                    type : answer.type,
                });
                // console.log("👈 ANSWER 생성", answer.sdp);

            } else if (signalType === "ANSWER") {
                // console.log("👉 ANSWER 받음", sdp);
                if (!sdp) {
                    console.warn("유효하지 않은 ANSWER 수신: sdp 없음");
                    return;
                }

                // OFFER를 보낸 측만 ANSWER를 remote으로 설정해야 함
                if (pc.signalingState === "have-local-offer") {
                    await pc.setRemoteDescription(new RTCSessionDescription({ type: "answer", sdp }));
                } else {
                    console.warn("잘못된 상태에서 answer를 remote으로 설정 시도됨. 현재 상태:", pc.signalingState);
                }

                if (pendingCandidates.current[senderId]) {
                    for (const ice of pendingCandidates.current[senderId]) {
                        try {
                            await pc.addIceCandidate(ice);
                        } catch (err) {
                            console.error("큐 처리 중 ICE candidate 추가 실패", err);
                        }
                    }
                    pendingCandidates.current[senderId] = [];
                }

            } else if (signalType === "CANDIDATE") {
                // console.log("💠 ICE 수신", candidate);
                // console.log("수신한 ICE candidate", candidate, sdpMid, sdpMLineIndex);

                if (!candidate || sdpMid == null || sdpMLineIndex == null) {
                    console.warn("ICE candidate 누락", { candidate, sdpMid, sdpMLineIndex });
                    return;
                }

                const ice = new RTCIceCandidate({ candidate, sdpMid, sdpMLineIndex });

                if (!pc.remoteDescription || !pc.remoteDescription.type) {
                    // 아직 remoteDescription 설정 전이면 큐에 넣음
                    if (!pendingCandidates.current[senderId]) {
                        pendingCandidates.current[senderId] = [];
                    }
                    pendingCandidates.current[senderId].push(ice);
                } else {
                    try {
                        await pc.addIceCandidate(ice);
                    } catch (err) {
                        console.error("ICE candidate 추가 실패: ", err);
                    }
                }
            }

        } catch (err) {
            console.error("시그널 처리 중 에러:", err);
        }
    };


    const leaveRoom = (roomId) => {
        // 로컬 트랙 중지
        if (localStream.current) {
            localStream.current.getTracks().forEach((track) => track.stop());
            localStream.current = null;
        }

        if (cameraStream.current) {
            cameraStream.current.getTracks().forEach(track => track.stop());
            cameraStream.current = null;
        }

        // 각 PeerConnection 종료
        Object.values(peerConnections.current).forEach((pc) => {
            pc.close();
        });
        peerConnections.current = {};

        // 화면 공유 상태 초기화
        setIsScreenSharing(false);

        // 캠/마이크 상태 초기화
        setIsCamOn(false);
        setIsMicOn(false);

        // 내 비디오 태그 비우기
        const localVideo = document.getElementById(`video-${userId}`);
        if (localVideo) {
            localVideo.srcObject = null;
        }

        // WebSocket 연결 종료
        disconnectWebSocket(roomId);
    }

    return {
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
    };
}
