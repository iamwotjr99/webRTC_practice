package com.practice.webRTC.signaling.application;

import lombok.Getter;

@Getter
public enum SignalType {
    OFFER,
    ANSWER,
    CANDIDATE
}
