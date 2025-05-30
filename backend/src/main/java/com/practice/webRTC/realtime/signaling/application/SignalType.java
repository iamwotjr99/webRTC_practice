package com.practice.webRTC.realtime.signaling.application;

import lombok.Getter;

@Getter
public enum SignalType {
    OFFER,
    ANSWER,
    CANDIDATE
}
