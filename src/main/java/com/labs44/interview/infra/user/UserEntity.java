package com.labs44.interview.infra.user;

import com.labs44.interview.domain.user.Authority;
import com.labs44.interview.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority role;

    @Column(name = "deleted", nullable = false)
    private int deleted;

    @Column(name = "created_at", nullable = false)
    private String created_at;

    @Column(name = "updated_at", nullable = false)
    private String updated_at;

    @Column(name = "deleted_at")
    private String deleted_at;

    public static User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.id)
                .email(entity.email)
                .nickname(entity.nickname)
                .password(entity.password)
                .deleted(entity.deleted)
                .role(entity.role)
                .created_at(entity.created_at)
                .updated_at(entity.updated_at)
                .build();
    }

    public static List<User> toDomainList(List<UserEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static UserEntity toEntity(User domain) {
        return UserEntity.builder()
                .id(domain.getId())
                .email(domain.getEmail())
                .nickname(domain.getNickname())
                .password(domain.getPassword())
                .role(domain.getRole())
                .created_at(domain.getCreated_at())
                .updated_at(domain.getUpdated_at())
                .build();
    }
}
