package com.practice.webRTC.room.ui;

import com.practice.webRTC.auth.infrastructure.security.userdetails.CustomUserDetails;
import com.practice.webRTC.global.common.ApiResponse;
import com.practice.webRTC.global.common.ResponseFactory;
import com.practice.webRTC.room.application.RoomService;
import com.practice.webRTC.room.application.dto.CreateRoomReqDto;
import com.practice.webRTC.room.domain.Room;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    // 방 입장/생성/조회(모든 방, 최근에 들어간 순(5건) 조회)

    // 방 입장
    @PostMapping("/join/{roomId}")
    public ResponseEntity<ApiResponse<Long>> joinRoom(@PathVariable Long roomId, @AuthenticationPrincipal
            CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        roomService.joinRoom(userId, roomId);

        return ResponseFactory.ok("방 입장 성공", roomId);
    }

    // 방 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Room>> createRoom(@RequestBody CreateRoomReqDto roomReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Room result = roomService.createRoom(userDetails.id(), roomReqDto);

        return ResponseFactory.ok("방 생성 성공", result);
    }

    // 내가 가입한 모든 방 조회
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<RoomListResDto>>> getMyRoomList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        List<Room> roomList = roomService.getMyRoomList(userId);
        List<RoomListResDto> result = roomList.stream()
                .map(room -> RoomListResDto.from(room)).toList();

        return ResponseFactory.ok("가입한 모든 방 조회 성공", result);
    }
    
    // 내가 최근에 들어간 방 조회 (5건)
    @GetMapping("/my/recent")
    public ResponseEntity<ApiResponse<List<RoomListResDto>>> getTop5MyRoomListByJoinedAt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        List<Room> roomList = roomService.getMyRoomListByJoinedAt(userId);
        List<RoomListResDto> result = roomList.stream()
                .map(room -> RoomListResDto.from(room)).toList();

        return ResponseFactory.ok("최근에 들어간 방 조회 성공", result);
    }
}
