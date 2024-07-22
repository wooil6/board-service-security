package com.springboot.board.dto;

import com.springboot.board.entity.Board;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private long memberId;

        private Board.BoardStatus boardStatus;

        public Member getMember() {
            Member member = new Member();
            member.setMemberId(memberId);
            return member;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private long boardId;
        private Board.QuestionStatus questionStatus;
        private Board.BoardStatus boardStatus;

        @NotBlank
        private String title;
        @NotBlank
        private String content;

        public void setBoardId (long boardId) {
            this.boardId = boardId;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private long boardId;
        private long memberId;
        private String title;
        private String content;
        private int view;
        private int likeCount;
        private LocalDateTime createAt;
        private LocalDateTime modifiedAt;
        private Board.BoardStatus boardStatus;
        private Board.QuestionStatus questionStatus;
        private List<CommentDto.Response> comments;

    }
}
