package com.practice.webRTC.room.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.practice.webRTC.Room.domain.Room;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Room 도메인 테스트")
class RoomTest {

    @Test
    @DisplayName("createRoom()으로 Room을 정상 생성")
    void givenRoom_whenRightRoom_thenCreateRoom() {
        Room room = Room.createRoom(1L, "백엔드 스터디 방", 5);
        assertThat(room.getUserId()).isEqualTo(1L);
        assertThat(room.getTitleValue()).isEqualTo("백엔드 스터디 방");
        assertThat(room.getCapacityValue()).isEqualTo(5);
        assertThat(room.getParticipant()).isZero();
    }

    @Test
    @DisplayName("참가자 수가 정원보다 적을 때 addParticipant() 호출하면 참가자 수 증가")
    void givenAddParticipant_whenParticipantUnderCapacity_thenAddParticipant() {
        Room room = Room.createRoom(1L, "백엔드 스터디 방", 5);
        assertThat(room.getParticipant()).isEqualTo(0);
        room.addParticipant();
        assertThat(room.getParticipant()).isEqualTo(1);
    }

    @Test
    @DisplayName("정원을 초과할 때 addParticipant()하면 예외 발생")
    void givenAddParticipant_whenParticipantOverCapacity_thenThrowFullException() {
        Room room = Room.createRoom(1L, "백엔드 스터디 방", 2);
        room.addParticipant();
        room.addParticipant();
        assertThatThrownBy(() -> room.addParticipant())
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_IS_FULL.getMessage());
    }

    @Test
    @DisplayName("참가자 수가 1이상일 때 정원보다 적을 때 removeParticipant() 호출하면 참가자 수 감소")
    void givenRemoveParticipant_whenParticipantOver1_thenRemoveParticipant() {
        Room room = Room.createRoom(1L, "백엔드 스터디 방", 5);
        room.addParticipant();
        room.addParticipant();
        assertThat(room.getParticipant()).isEqualTo(2);
        room.removeParticipant();
        assertThat(room.getParticipant()).isEqualTo(1);
    }

    @Test
    @DisplayName("참가자 수가 0일 때 removeParticipant() 호출해도 참가자 수 0")
    void givenRemoveParticipant_whenParticipantIsZero_thenParticipantIsZero() {
        Room room = Room.createRoom(1L, "백엔드 스터디 방", 5);
        room.removeParticipant();
        assertThat(room.getParticipant()).isZero();
    }
}
