package com.springboot.comment.entity;

import com.springboot.member.entity.Member;
import com.springboot.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

//    @Enumerated(value = EnumType.STRING)
//    private Board.BoardStatus commentStatus;


    public void setBoard(Board board) {
        this.board = board;
        if (!board.getComments().contains(this)) {
            board.setComment(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (!member.getComments().contains(this)) {
            member.setComment(this);

        }
    }

    @Enumerated(value = EnumType.STRING)
    private Board.BoardStatus commentStatus = Board.BoardStatus.PUBLIC;

//
//    public enum CommentStatus {
//        PUBLIC("공개글 상태"),
//        SECRET("비밀글 상태");
//
//        @Getter
//        private String status;
//
//
//        CommentStatus(String status) {
//            this.status = status;
////        }
//    }
}
