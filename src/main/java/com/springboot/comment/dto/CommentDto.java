package com.springboot.comment.dto;

import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String content;
        private long memberId;
        private long boardId;
      //  private Board.BoardStatus commentStatus;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private long commentId;

        @NotBlank
        private String content;

        public void setComment(long commentId) {
            this.commentId = commentId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long commentId;
        private long boardId;
        private long memberId;
        private LocalDateTime createAt;
        private LocalDateTime modifiedAt;
        private String content;
        private Board.BoardStatus commentStatus;
    }


}
