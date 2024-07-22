package com.springboot.board.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.comment.service.CommentService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;



    public BoardService(BoardRepository boardRepository,
                        MemberService memberService) {
        this.boardRepository = boardRepository;
        this.memberService = memberService;
    }

    // 게시판 글 등록
    public Board createBoard(Board board) {
        //Member member =
                memberService.findVerifiedMember(board.getMember().getMemberId());
//        member.setPost(board);
//        board.setMember(member);
        board.setCreateAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    // @Transactional
    // 글 수정
    // 글이 있는지 먼저 조회
    public Board updateBoard(Board board) {

        Board findBoard = findVerifiedBoard(board.getBoardId());

        Optional.ofNullable(board.getQuestionStatus())
                .ifPresent(questionStatus -> findBoard.setQuestionStatus(questionStatus));
        Optional.ofNullable(board.getBoardStatus())
                .ifPresent(boardStatus -> findBoard.setBoardStatus(boardStatus));
        Optional.ofNullable(board.getTitle())
                .ifPresent(title -> findBoard.setTitle(title));
        Optional.ofNullable(board.getContent())
                .ifPresent(content -> findBoard.setContent(content));

        findBoard.setModifiedAt(LocalDateTime.now());

        // findBoard에 댓글이 있는지 확인
            List<Comment> findComment = findBoard.getComments();
            if (findComment != null) {
                if (findBoard.getBoardStatus() == Board.BoardStatus.PUBLIC) {
                    findComment.forEach(comment -> comment.setCommentStatus(Board.BoardStatus.PUBLIC));
                } else {
                    findComment.forEach(comment -> comment.setCommentStatus(Board.BoardStatus.SECRET));
                }
            }

        return boardRepository.save(findBoard);
    }




   // @Transactional
    // 글 조회
    public Board findBoard(long boardId) {
        Board board = findVerifiedBoard(boardId);
        board.setView(board.getView()+1);
        return board;
       // return findVerifiedBoard(boardId);
    }

    public Page<Board> findMemberBoards(long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("boardId").descending());
        return boardRepository.findByMember_MemberId(memberId, pageable);
    }

//    public Page<Board> findBoards(int page, int size) {
//        return boardRepository.findAll(PageRequest.of(page, size,
//                Sort.by("boardId").descending()));
//    }

//    public Page<Board> findBoardsCreateAT(int page, int size) {
//        return boardRepository.findAll(PageRequest.of(page, size,
//                Sort.by("createAt").descending()));
//    }

    public Page<Board> findBoardsSort(int page, int size, Sort sort) {
        return boardRepository.findAll(PageRequest.of(page, size, sort));
    }


    // 글 삭제
    public void deleteBoard(long boardId) {
        Board findBoard = findVerifiedBoard(boardId);
        Board.QuestionStatus boardStatus= findBoard.getQuestionStatus();
        if (boardStatus == Board.QuestionStatus.QUESTION_REGISTERED || boardStatus == Board.QuestionStatus.QUESTION_ANSWERED) {
            findBoard.setQuestionStatus(Board.QuestionStatus.QUESTION_DELETED);
        }
        boardRepository.save(findBoard);
    }


    // 글이 있는지 확인
    public Board findVerifiedBoard(long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board findBoard = optionalBoard.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        return findBoard;
        }


    //    public void setCommentStatus(Comment comment) {
//        comment.setCommentStatus(comment.getBoard().getBoardStatus());
//    }
//    private void updateCommentStatus(Board board, Board.BoardStatus boardStatus) {
//
//    }

//    private void updateCommentsStatus(Board board, Board.BoardStatus boardStatus) {
//        Comment.CommentStatus commentStatus = boardStatus == Board.BoardStatus.PUBLIC
//                ? Comment.CommentStatus.PUBLIC
//                : Comment.CommentStatus.SECRET;
//
//        board.getComments().forEach(comment -> {
//            comment.setCommentStatus(commentStatus);
//            commentService.updateComment(comment);
//        });
//    }

//    public Board findVerifiedBoardComments(long boardId) {
//        Optional<Board> optionalBoardComments = boardRepository.findByCommentId()
//    }

}
