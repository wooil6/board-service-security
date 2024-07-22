package com.springboot.comment.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.board.service.BoardService;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    public CommentService(CommentRepository commentRepository,
                          CommentMapper mapper,
                          BoardService boardService,
                          BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }

    public Comment createComment(Comment comment) {
        comment.setCreateAt(LocalDateTime.now());
        // 보드의 상태가  QUESTION_ANSWERED로 변경
        Board findBoard = boardService.findVerifiedBoard(comment.getBoard().getBoardId());
        findBoard.setQuestionStatus(Board.QuestionStatus.QUESTION_ANSWERED);
        comment.setCommentStatus(findBoard.getBoardStatus());
//        if (findBoard.getBoardStatus() == Board.BoardStatus.PUBLIC) {
//            comment.setCommentStatus(Comment.CommentStatus.PUBLIC);
//        } else {
//            comment.setCommentStatus(Comment.CommentStatus.SECRET);
//        }
        boardRepository.save(findBoard);
        return commentRepository.save(comment);
    }

//    public Comment createComment(Comment comment, Board board) {
//
//        comment.setCreateAt(LocalDateTime.now());
//        // 보드의 상태가  QUESTION_ANSWERED로 변경
//        Board findBoard = boardService.findVerifiedBoard(comment.getBoard().getBoardId());
//        board.setQuestionStatus(Board.QuestionStatus.QUESTION_ANSWERED);
//        //comment.setCommentStatus(findBoard.getBoardStatus());
////        if (board.getBoardStatus() == Board.BoardStatus.PUBLIC) {
////            comment.setCommentStatus(Comment.CommentStatus.PUBLIC);
////        } else {
////            comment.setCommentStatus(Comment.CommentStatus.SECRET);
////        }
//        boardRepository.save(board);
//        return commentRepository.save(comment);
//    }


    public Comment updateComment(Comment comment) {
        Comment findComment = findeverifiedComment(comment.getCommentId());

        Optional.ofNullable(comment.getContent())
                .ifPresent(content -> findComment.setContent(content));
        findComment.setModifiedAt(LocalDateTime.now());

        return commentRepository.save(findComment);
    }

//    public  Comment updateCommentStatus(Comment comment) {
//        comment.setCommentStatus(comment.getCommentStatus());
//        return commentRepository.save(comment);
//    }

//    public Board.BoardStatus setCommentStatus(Board board) {
//        Board.BoardStatus commentStatus = boardService.updateBoard(board).getBoardStatus();
//        return commentStatus;
//    }

//    public Board.BoardStatus setCommentStatusPublic(Board board) {
//        Board.BoardStatus publicStatus = Board.BoardStatus.PUBLIC;
//        return publicStatus;
//    }
//
//    public Board.BoardStatus setCommentStatusSecret(Board board) {
//        Board.BoardStatus secretStatus = Board.BoardStatus.SECRET;
//        return secretStatus;
//    }


    public Comment findComment(long commentId) {
        return findeverifiedComment(commentId);
    }

    public Page<Comment> findComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size,
                Sort.by("commentId").descending()));
    }


    public void deleteComment(long commentId) {
        Comment findComment = findeverifiedComment(commentId);
        commentRepository.delete(findComment);
    }

    // 댓글이 있는지 확인
    public Comment findeverifiedComment(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment findComment = optionalComment.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        return findComment;
    }
}
