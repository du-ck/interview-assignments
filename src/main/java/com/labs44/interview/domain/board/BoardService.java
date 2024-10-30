package com.labs44.interview.domain.board;


import com.labs44.interview.support.exception.AlreadyExistBoardException;
import com.labs44.interview.support.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    public List<Board> getBoards(int page, int size) throws Exception {
        List<Board> boards = boardRepository.findAll(page, size);
        if (CollectionUtils.isEmpty(boards)) {
            throw new ResourceNotFoundException("검색결과 없음");
        }
        return boards;
    }

    public List<Post> getPosts(int boardId, int page, int size) throws Exception {
        List<Post> posts = postRepository.findByBoardId(boardId, page, size);
        if (CollectionUtils.isEmpty(posts)) {
            throw new ResourceNotFoundException("검색결과 없음");
        }
        return posts;
    }

    public Optional<Board> addBoard(String name, String description) throws Exception {

        //이미 존재하는 게시판인지 체크
        Optional<Board> findBoard = boardRepository.findByName(name);
        if (findBoard.isPresent()) {
            throw new AlreadyExistBoardException("이미 존재하는 게시판 입니다");
        }

        Board createBoard = Board.builder().build();

        //게시판 생성 데이터 처리
        createBoard.createdBoard(name, description);

        Optional<Board> saveBoard = boardRepository.save(createBoard);
        if (!saveBoard.isPresent()) {
            throw new Exception("게시판 생성 실패");
        }
        return saveBoard;
    }

    public Optional<Post> updatePost(Post updatePost) throws Exception {

        //존재하는 post인지 조회
        Optional<Post> findPost = postRepository.findById(updatePost.getId());
        if (!findPost.isPresent()) {
            throw new IllegalArgumentException("없는 post id 입니다");
        }
        Post findPostGet = findPost.get();
        //post update 데이터 처리
        findPostGet.updatePost(updatePost);

        Optional<Post> post = postRepository.save(findPostGet);
        if (!post.isPresent()) {
            throw new Exception("게시글 수정 실패");
        }
        return post;
    }
}
