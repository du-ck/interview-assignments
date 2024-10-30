package com.labs44.interview.infra.board;

import com.labs44.interview.domain.board.Board;
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
@Table(name = "board")
public class BoardEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 20, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private String created_at;

    @Column(name = "updated_at", nullable = false)
    private String updated_at;

    public static Board toDomain(BoardEntity entity) {
        return Board.builder()
                .id(entity.id)
                .name(entity.name)
                .description(entity.description)
                .created_at(entity.created_at)
                .updated_at(entity.updated_at)
                .build();
    }

    public static List<Board> toDomainList(List<BoardEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static BoardEntity toEntity(Board domain) {
        return BoardEntity.builder()
                .name(domain.getName())
                .description(domain.getDescription())
                .created_at(domain.getCreated_at())
                .updated_at(domain.getUpdated_at())
                .build();
    }
}
