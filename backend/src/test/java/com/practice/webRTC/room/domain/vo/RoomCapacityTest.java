package com.practice.webRTC.room.domain.vo;

import com.practice.webRTC.Room.domain.vo.RoomCapacity;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RoomCapacity 유효성 테스트")
class RoomCapacityTest {

    @Test
    @DisplayName("총 인원수가 2보다 작으면 에러 발생")
    void givenCapacity_whenCapacityUnder2_thenThrowCapacityUnder2Error() {
        Assertions.assertThatThrownBy(() -> new RoomCapacity(0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_CAPACITY_UNDER_MIN.getMessage());
    }

    @Test
    @DisplayName("총 인원수가 6보다 크면 에러 발생")
    void givenCapacity_whenCapacityOver6_thenThrowCapacityOver6Error() {
        Assertions.assertThatThrownBy(() -> new RoomCapacity(7))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_CAPACITY_OVER_MAX.getMessage());
    }

    @Test
    @DisplayName("총 인원수가 2보다 크고 6보다 작으면 정상 생성")
    void givenCapacity_whenRightCapacity_thenCreateRoomCapacity() {
        RoomCapacity roomCapacity = new RoomCapacity(4);
        Assertions.assertThat(roomCapacity.getValue()).isEqualTo(4);
    }
}
