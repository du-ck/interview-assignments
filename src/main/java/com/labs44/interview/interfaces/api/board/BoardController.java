package com.labs44.interview.interfaces.api.board;

import com.labs44.interview.domain.board.Board;
import com.labs44.interview.domain.board.BoardService;
import com.labs44.interview.domain.board.Post;
import com.labs44.interview.interfaces.api.dto.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 목록
     */
    @GetMapping("/")
    public ResponseEntity<ResponseData> getBoards(@Valid GetBoards.Request req) throws Exception {

        List<Board> boards = boardService.getBoards(req.getPage(), req.getSize());

        GetBoards.Response response = GetBoards.Response
                .builder()
                .boards(boards)
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

    /**
     * 선택된 게시판의 글 목록
     */
    @GetMapping("/posts")
    public ResponseEntity<ResponseData> getPosts(@Valid GetPosts.Request req) throws Exception {

        List<Post> posts = boardService.getPosts(req.getBoardId(), req.getPage(), req.getSize());

        GetPosts.Response response = GetPosts.Response.builder()
                .posts(posts)
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

    /**
     * 게시판 생성
     */
    @PostMapping
    public ResponseEntity<ResponseData> addBoard(@RequestBody @Valid AddBoard.Request req) throws Exception {

        Optional<Board> board = boardService.addBoard(req.name, req.description);

        AddBoard.Response response = AddBoard.Response.builder()
                .board(board.get())
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

    /**
     * 게시판 글 업데이트
     */
    @PutMapping("/posts/update")
    public ResponseEntity<ResponseData> updatePost(@RequestBody @Valid UpdatePost.Request req) throws Exception {
        Post updatePost = Post.builder()
                .id(req.getPostId())
                .boardId(req.getBoardId())
                .userId(req.getUserId())
                .title(req.getTitle())
                .content(req.getContent())
                .build();

        Optional<Post> post = boardService.updatePost(updatePost);

        UpdatePost.Response response = UpdatePost.Response.builder()
                .post(post.get())
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }
}
