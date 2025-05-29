package com.practice.webRTC.signaling.application.dto;

import com.practice.webRTC.signaling.application.SignalType;

public record SignalingMessageDto(
        Long senderId,
        Long targetId,          // offer를 보낼 상대 id

        SignalType signalType,  // offer/answer/candidate

        String sdp,             // Session Description Protocol : 내가 어떤 연결을 할 수 있는지 설명하는 명세서
        String candidate,       // ICE 후보 IP + port 정보
        String sdpMid,          // SDP의 media ID
        Integer sdpMLineIndex   // 미디어 라인 순서(index) ex) 오디오: sdpMLineIndex=0 비디오: sdpMLineIndex=1
) {

    public SignalType getType() {
        return this.signalType;
    }
}
