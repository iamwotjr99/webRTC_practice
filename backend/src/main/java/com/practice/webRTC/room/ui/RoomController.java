package com.practice.webRTC.room.ui;

import com.practice.webRTC.auth.infrastructure.security.userdetails.CustomUserDetails;
import com.practice.webRTC.global.common.ApiResponse;
import com.practice.webRTC.global.common.ResponseFactory;
import com.practice.webRTC.room.application.RoomService;
import com.practice.webRTC.room.application.dto.CreateRoomReqDto;
import com.practice.webRTC.room.application.dto.EnterRoomResDto;
import com.practice.webRTC.room.application.dto.JoinRoomResDto;
import com.practice.webRTC.room.domain.Room;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Log4j2
public class RoomController {
    private final RoomService roomService;

    // 방 입장/생성/조회(모든 방, 최근에 들어간 순(5건) 조회)

    // 방 입장
    @PostMapping("/join/{roomId}")
    public ResponseEntity<ApiResponse<JoinRoomResDto>> joinRoom(@PathVariable Long roomId, @AuthenticationPrincipal
            CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        JoinRoomResDto result = roomService.joinRoom(userId, roomId);

        return ResponseFactory.ok("방 입장 성공", result);
    }

    // 방 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Room>> createRoom(@RequestBody CreateRoomReqDto roomReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Room result = roomService.createRoom(userDetails.id(), roomReqDto);

        return ResponseFactory.ok("방 생성 성공", result);
    }

    // 방 입장
    @PostMapping("/enter/{roomId}")
    public ResponseEntity<ApiResponse<EnterRoomResDto>> enterRoom(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        EnterRoomResDto result = roomService.enterRoom(userId, roomId);

        return ResponseFactory.ok("방 입장 성공", result);
    }

    // 방 퇴장 (완전 퇴장 x)
    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<ApiResponse<Void>> leaveRoom(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        roomService.leaveRoom(userId, roomId);

        return ResponseFactory.ok("방 퇴장 성공", null);
    }

    // 내가 가입한 모든 방 조회
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<RoomListResDto>>> getMyRoomList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.id();

        List<Room> roomList = roomService.getMyRoomList(userId);

        List<RoomListResDto> result = roomList.stream()
                .map(RoomListResDto::from).toList();

        return ResponseFactory.ok("가입한 모든 방 조회 성공", result);
    }
    
    // 내가 최근에 들어간 방 조회 (5건)
    @GetMapping("/my/recent")
    public ResponseEntity<ApiResponse<List<RoomListResDto>>> getTop5MyRoomListByJoinedAt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.id();
        log.info("[RECENT] userId: {}", userId);

        List<Room> roomList = roomService.getMyRoomListByJoinedAt(userId);
        List<RoomListResDto> result = roomList.stream()
                .map(RoomListResDto::from).toList();

        return ResponseFactory.ok("최근에 들어간 방 조회 성공", result);
    }
}
