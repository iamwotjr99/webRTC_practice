package com.practice.webRTC.Room.domain.entity;

import com.practice.webRTC.Room.domain.Room;
import com.practice.webRTC.Room.domain.vo.RoomCapacity;
import com.practice.webRTC.Room.domain.vo.RoomTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private RoomTitleEntity titleEntity;

    @Embedded
    private RoomCapacityEntity capacityEntity;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public RoomEntity(Room room) {
        this.id = room.getId();
        this.userId = room.getUserId();
        this.titleEntity = new RoomTitleEntity(room.getTitleValue());
        this.capacityEntity = new RoomCapacityEntity(room.getCapacityValue());
    }

    public Room toRoom(int participant) {
        return Room.builder()
                .id(this.id)
                .userId(this.userId)
                .title(new RoomTitle(this.titleEntity.getTitle()))
                .capacity(new RoomCapacity(this.capacityEntity.getCapacity()))
                .createdAt(this.createdAt)
                .participant(participant)
                .build();
    }
}
