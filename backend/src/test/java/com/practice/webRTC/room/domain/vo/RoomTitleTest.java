package com.practice.webRTC.room.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RoomTitle 유효성 테스트")
class RoomTitleTest {

    @Test
    @DisplayName("비어있는 제목이면 예외 발생")
    void givenTitle_whenTitleIsNull_thenThrowNullTitleError() {
        Assertions.assertThatThrownBy(() -> new RoomTitle(""))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_TITLE_ESSENTIAL.getMessage());
    }

    @Test
    @DisplayName("제목이 2글자보다 작으면 예외 발생")
    void givenTitle_whenLengthUnder2_thenThrowUnder2Error() {
        Assertions.assertThatThrownBy(() -> new RoomTitle("한"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_TITLE_TOO_SHORT.getMessage());
    }

    @Test
    @DisplayName("제목이 20글자보다 크면 예외 발생")
    void givenTitle_whenLengthOver20_thenThrowOver20Error() {
        String over20Title = "123456789101112131415151232132134";
        Assertions.assertThatThrownBy(() -> new RoomTitle(over20Title))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_TITLE_TOO_LONG.getMessage());
    }

    @Test
    @DisplayName("제목이 2글자 이상 20자 이하면 정상 생성")
    void givenTitle_whenRightTitle_thenCreateRoomTitle() {
        RoomTitle roomTitle = new RoomTitle("백엔드 스터디 하실 분~");
        Assertions.assertThat(roomTitle.value()).isEqualTo("백엔드 스터디 하실 분~");
    }
}
