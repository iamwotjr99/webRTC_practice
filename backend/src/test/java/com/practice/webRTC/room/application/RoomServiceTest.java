package com.practice.webRTC.room.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.practice.webRTC.room.application.dto.CreateRoomReqDto;
import com.practice.webRTC.room.domain.Room;
import com.practice.webRTC.room.domain.repository.RoomRepository;
import com.practice.webRTC.room.domain.vo.RoomCapacity;
import com.practice.webRTC.room.domain.vo.RoomTitle;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Test
    @DisplayName("채팅방 생성 성공")
    void givenCreateRoomReqDto_whenCreatRoom_thenCreatRoomSuccess() {
        // given
        CreateRoomReqDto dto = CreateRoomReqDto.from(1L, "백엔드 스터디 같이합시다~", 4);

        // when
        roomService.createRoom(dto);

        // then
        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository, times(1)).save(captor.capture());

        Room saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(1L);
        assertThat(saved.getTitleValue()).isEqualTo("백엔드 스터디 같이합시다~");
        assertThat(saved.getParticipant()).isZero();
        assertThat(saved.getCapacityValue()).isEqualTo(4);
    }

    @Test
    @DisplayName("roomId로 방 조회 성공")
    void givenRoomId_whenGetRoom_thenReturnRoomSuccess() {
        // given
        Room room = Room.builder()
                .id(1L)
                .userId(1L)
                .title(new RoomTitle("백엔드 스터디 같이 합시다~"))
                .capacity(new RoomCapacity(4))
                .participant(0)
                .createdAt(LocalDateTime.now())
                .build();

        Long roomId = room.getId();
        when(roomRepository.findById(roomId)).thenReturn(room);

        // when
        Room result = roomService.getRoom(roomId);

        // then
        verify(roomRepository, times(1)).findById(roomId); // 조회 요청이 실제로 일어났는지 확인
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getTitleValue()).isEqualTo("백엔드 스터디 같이 합시다~");
        assertThat(result.getParticipant()).isZero();
        assertThat(result.getCapacityValue()).isEqualTo(4);
    }

    @Test
    @DisplayName("존재하지 않는 roomId로 방 조회 시 예외 발생")
    void givenInValidRoomId_whenGetRoom_thenThrowException() {
        // given
        Long invalidRoomId = 999L;

        when(roomRepository.findById(invalidRoomId))
                .thenThrow(new CustomException(ErrorCode.ROOM_IS_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> roomService.getRoom(invalidRoomId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ROOM_IS_NOT_FOUND.getMessage());

        verify(roomRepository, times(1)).findById(invalidRoomId);
    }
}
