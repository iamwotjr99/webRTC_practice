package com.practice.webRTC.room.domain.entity;

import com.practice.webRTC.room.domain.RoomUser;
import com.practice.webRTC.user.domain.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "room_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @CreatedDate
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    public RoomUserEntity(UserEntity userEntity, RoomEntity roomEntity) {
        this.user = userEntity;
        this.room = roomEntity;
        this.joinedAt = LocalDateTime.now();
    }

    public void applyDomain(RoomUser roomUser) {
        this.joinedAt = roomUser.getJoinedAt();
    }

    public RoomUser toDomain() {
        return RoomUser.builder()
                .id(this.id)
                .userId(this.user.getId())
                .roomId(this.room.getId())
                .joinedAt(this.joinedAt)
                .build();
    }
}
