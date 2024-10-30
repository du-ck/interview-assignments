package com.labs44.interview.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labs44.interview.domain.board.Board;
import com.labs44.interview.domain.board.BoardService;
import com.labs44.interview.domain.board.Post;
import com.labs44.interview.interfaces.api.board.*;
import com.labs44.interview.support.config.SecurityConfig;
import com.labs44.interview.support.utils.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private BoardService boardService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @Test
    void getBoards() throws Exception {
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

        GetBoards.Request req = GetBoards.Request.builder()
                .page(0)
                .size(10)
                .build();

        given(boardService.getBoards(req.getPage(), req.getSize()))
                .willReturn(boards);

        mockMvc.perform(get("/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(req.getPage()))
                        .param("size", String.valueOf(req.getSize()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.boards").exists())
                .andExpect(jsonPath("$.data.boards[*].id").exists())
                .andExpect(jsonPath("$.data.boards[*].name").exists())
                .andExpect(jsonPath("$.data.boards[*].description").exists())
                .andExpect(jsonPath("$.data.boards[*].created_at").exists())
                .andExpect(jsonPath("$.data.boards[*].updated_at").exists());
    }

    @Test
    void getPosts() throws Exception {
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

        GetPosts.Request req = GetPosts.Request.builder()
                .boardId(1)
                .page(0)
                .size(10)
                .build();

        given(boardService.getPosts(req.getBoardId(), req.getPage(), req.getSize()))
                .willReturn(posts);

        mockMvc.perform(get("/boards/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("boardId", String.valueOf(req.getBoardId()))
                        .param("page", String.valueOf(req.getPage()))
                        .param("size", String.valueOf(req.getSize()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.posts").exists())
                .andExpect(jsonPath("$.data.posts[*].id").exists())
                .andExpect(jsonPath("$.data.posts[*].boardId").exists())
                .andExpect(jsonPath("$.data.posts[*].userId").exists())
                .andExpect(jsonPath("$.data.posts[*].title").exists())
                .andExpect(jsonPath("$.data.posts[*].content").exists())
                .andExpect(jsonPath("$.data.posts[*].deleted").exists())
                .andExpect(jsonPath("$.data.posts[*].created_at").exists())
                .andExpect(jsonPath("$.data.posts[*].updated_at").exists())
                .andExpect(jsonPath("$.data.posts[*].deleted_at").exists());
    }

    @Test
    void addBoard() throws Exception {
        AddBoard.Request req = AddBoard.Request.builder()
                .name("test1")
                .description("test11")
                .build();

        Board board = Board.builder()
                .id(1)
                .name(req.getName())
                .description(req.getDescription())
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();


        given(boardService.addBoard(req.getName(), req.getDescription()))
                .willReturn(Optional.of(board));

        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.board").exists())
                .andExpect(jsonPath("$.data.board.id").exists())
                .andExpect(jsonPath("$.data.board.name").exists())
                .andExpect(jsonPath("$.data.board.description").exists())
                .andExpect(jsonPath("$.data.board.created_at").exists())
                .andExpect(jsonPath("$.data.board.updated_at").exists());
    }

    @Test
    void addBoard_파라미터없음() throws Exception {
        AddBoard.Request req = AddBoard.Request.builder()
                .description("test11")
                .build();

        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("게시판 이름이 없습니다"));

        req = AddBoard.Request.builder()
                .name("test1")
                .build();

        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("게시판 설명이 없습니다"));
    }

    @Test
    void updatePost() throws Exception {
        UpdatePost.Request req = UpdatePost.Request.builder()
                .postId(1)
                .boardId(1)
                .userId(1)
                .title("test-update")
                .content("test-update")
                .build();

        Post post = Post.builder()
                .id(req.getPostId())
                .boardId(req.getBoardId())
                .userId(req.getUserId())
                .title(req.getTitle())
                .content(req.getContent())
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted_at(LocalDateTime.now().toString())
                .build();

        given(boardService.updatePost(any(Post.class)))
                .willReturn(Optional.of(post));

        mockMvc.perform(put("/boards/posts/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.post").exists())
                .andExpect(jsonPath("$.data.post.id").exists())
                .andExpect(jsonPath("$.data.post.boardId").exists())
                .andExpect(jsonPath("$.data.post.userId").exists())
                .andExpect(jsonPath("$.data.post.title").exists())
                .andExpect(jsonPath("$.data.post.content").exists())
                .andExpect(jsonPath("$.data.post.deleted").exists())
                .andExpect(jsonPath("$.data.post.created_at").exists())
                .andExpect(jsonPath("$.data.post.updated_at").exists())
                .andExpect(jsonPath("$.data.post.deleted_at").exists());
    }

    @Test
    void updatePost_파라미터없음() throws Exception {
        UpdatePost.Request req = UpdatePost.Request.builder()
                .postId(1)
                .boardId(1)
                .userId(1)
                .content("test-update")
                .build();

        mockMvc.perform(put("/boards/posts/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("제목이 없습니다"));

        req = UpdatePost.Request.builder()
                .postId(1)
                .boardId(1)
                .userId(1)
                .title("test-update")
                .build();

        mockMvc.perform(put("/boards/posts/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("내용이 없습니다"));
    }
}
