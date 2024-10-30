package com.labs44.interview.infra.board;

import com.labs44.interview.domain.board.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class PostEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "board_id", nullable = false)
    private int boardId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "deleted", nullable = false)
    private int deleted;

    @Column(name = "created_at", nullable = false)
    private String created_at;

    @Column(name = "updated_at", nullable = false)
    private String updated_at;

    @Column(name = "deleted_at", nullable = false)
    private String deleted_at;

    public static Post toDomain(PostEntity entity) {
        return Post.builder()
                .id(entity.id)
                .boardId(entity.boardId)
                .userId(entity.userId)
                .title(entity.title)
                .content(entity.content)
                .deleted(entity.deleted)
                .created_at(entity.created_at)
                .updated_at(entity.updated_at)
                .deleted_at(entity.deleted_at)
                .build();
    }

    public static List<Post> toDomainList(List<PostEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static PostEntity toEntity(Post domain) {
        return PostEntity.builder()
                .id(domain.getId())
                .boardId(domain.getBoardId())
                .userId(domain.getUserId())
                .title(domain.getTitle())
                .content(domain.getContent())
                .deleted(domain.getDeleted())
                .created_at(domain.getCreated_at())
                .updated_at(domain.getUpdated_at())
                .deleted_at(domain.getDeleted_at())
                .build();
    }

}
