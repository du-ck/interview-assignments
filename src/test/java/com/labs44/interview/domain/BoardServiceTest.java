package com.labs44.interview.domain;

import com.labs44.interview.domain.board.*;
import com.labs44.interview.support.exception.AlreadyExistBoardException;
import com.labs44.interview.support.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private PostRepository postRepository;


    @Test
    void 게시판목록_조회() throws Exception {
        List<Board> boards = new ArrayList<>();
        boards.add(Board.builder()
                .id(1)
                .name("test1")
                .description("test1")
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build());
        boards.add(Board.builder()
                .id(2)
                .name("test2")
                .description("test2")
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build());

        given(boardRepository.findAll(anyInt(), anyInt()))
                .willReturn(boards);

        List<Board> result = boardService.getBoards(anyInt(), anyInt());

        Assertions.assertEquals(boards.size(), result.size());
    }

    @Test
    void 게시판목록_조회_결과없음() throws Exception {

        List<Board> boards = new ArrayList<>();
        given(boardRepository.findAll(anyInt(), anyInt()))
                .willReturn(boards);

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> boardService.getBoards(anyInt(), anyInt()));

        Assertions.assertEquals("검색결과 없음", exception.getMessage());
    }

    @Test
    void 게시판_글목록_조회() throws Exception {
        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder()
                .id(1)
                .boardId(1)
                .userId(1)
                .title("post1")
                .content("post1")
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted_at(LocalDateTime.now().toString())
                .build());
        posts.add(Post.builder()
                .id(2)
                .boardId(1)
                .userId(1)
                .title("post2")
                .content("post2")
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted_at(LocalDateTime.now().toString())
                .build());

        given(postRepository.findByBoardId(anyInt(), anyInt(), anyInt()))
                .willReturn(posts);

        List<Post> result = boardService.getPosts(anyInt(), anyInt(), anyInt());

        Assertions.assertEquals(posts.size(), result.size());
    }

    @Test
    void 게시판_글목록_조회_결과없음() throws Exception {

        List<Post> posts = new ArrayList<>();
        given(postRepository.findByBoardId(anyInt(), anyInt(), anyInt()))
                .willReturn(posts);

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> boardService.getPosts(anyInt(), anyInt(), anyInt()));

        Assertions.assertEquals("검색결과 없음", exception.getMessage());
    }

    @Test
    void 게시판_생성() throws Exception {
        String name = "test1";
        String description = "test11";
        Board board = Board.builder()
                .id(1)
                .name(name)
                .description(description)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();

        given(boardRepository.findByName(anyString()))
                .willReturn(Optional.empty());

        given(boardRepository.save(any(Board.class)))
                .willReturn(Optional.of(board));

        Optional<Board> saveBoard = boardService.addBoard(name, description);

        Assertions.assertTrue(saveBoard.isPresent());
    }

    @Test
    void 게시판_생성_이미존재하는게시판() throws Exception {
        String name = "test1";
        String description = "test11";
        Board board = Board.builder()
                .id(1)
                .name(name)
                .description(description)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();

        given(boardRepository.findByName(anyString()))
                .willReturn(Optional.of(board));

        Exception exception = Assertions.assertThrows(AlreadyExistBoardException.class,
                () -> boardService.addBoard(name, description));

        Assertions.assertEquals("이미 존재하는 게시판 입니다", exception.getMessage());
    }

    @Test
    void 게시판_생성_실패() throws Exception {
        String name = "test1";
        String description = "test11";
        Board board = Board.builder()
                .id(1)
                .name(name)
                .description(description)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();

        given(boardRepository.findByName(anyString()))
                .willReturn(Optional.empty());

        given(boardRepository.save(any(Board.class)))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> boardService.addBoard(name, description));

        Assertions.assertEquals("게시판 생성 실패", exception.getMessage());
    }

    @Test
    void 글_업데이트() throws Exception {
        String title = "test-update-title";
        String content = "test-update-content";

        Post post = Post.builder()
                .id(1)
                .boardId(1)
                .userId(1)
                .title(title)
                .content(content)
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted_at(LocalDateTime.now().toString())
                .build();

        given(postRepository.findById(anyInt()))
                .willReturn(Optional.of(post));

        given(postRepository.save(post))
                .willReturn(Optional.of(post));

        Optional<Post> updatePost = boardService.updatePost(post);
        Assertions.assertTrue(updatePost.isPresent());
    }

    @Test
    void 글_업데이트_없는_postId() throws Exception {
        String title = "test-update-title";
        String content = "test-update-content";

        Post post = Post.builder()
                .id(1)
                .boardId(1)
                .userId(1)
                .title(title)
                .content(content)
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted_at(LocalDateTime.now().toString())
                .build();

        given(postRepository.findById(anyInt()))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> boardService.updatePost(post));

        Assertions.assertEquals("없는 post id 입니다", exception.getMessage());
    }

    @Test
    void 글_업데이트_실패() throws Exception {
        String title = "test-update-title";
        String content = "test-update-content";

        Post post = Post.builder()
                .id(1)
                .boardId(1)
                .userId(1)
                .title(title)
                .content(content)
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted_at(LocalDateTime.now().toString())
                .build();

        given(postRepository.findById(anyInt()))
                .willReturn(Optional.of(post));

        given(postRepository.save(post))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> boardService.updatePost(post));

        Assertions.assertEquals("게시글 수정 실패", exception.getMessage());
    }
}
