package com.practice.webRTC.user.domain.entity;

import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.vo.Nickname;
import com.practice.webRTC.user.domain.vo.Password;
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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NicknameEntity nicknameEntity;

    @Embedded
    private PasswordEntity passwordEntity;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public UserEntity(User user) {
        this.id = user.getId();
        this.nicknameEntity = new NicknameEntity(user.getNicknameValue());
        this.passwordEntity = new PasswordEntity(user.getPasswordValue());
        this.createdAt = user.getCreatedAt();
    }

    public User toUser() {
        return User.builder()
                .id(this.id)
                .nickname(new Nickname(nicknameEntity.getNickname()))
                .password(Password.fromEncodedValue(passwordEntity.getPassword()))
                .createdAt(this.createdAt)
                .build();
    }

}
