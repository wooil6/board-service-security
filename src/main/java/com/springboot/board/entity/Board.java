package com.springboot.board.entity;

import com.springboot.comment.entity.Comment;
import com.springboot.like.entity.Like;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Board {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int view = 0;

    @Column(nullable = false)
    private int likeCount = 0;


    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTERED;

    @Enumerated(value = EnumType.STRING)
    private BoardStatus boardStatus;


    public enum QuestionStatus {
        QUESTION_REGISTERED("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETED("질문 삭제 상태"),
        QUESTION_DEACTIVED("질문 비활성화 상태"),
        ;

        @Getter
        private String status;

        QuestionStatus(String status) {
            this.status = status;
        }
    }

    public enum BoardStatus {
        PUBLIC("공개글 상태"),
        SECRET("비밀글 상태");

        @Getter
        private String status;

        BoardStatus(String status) {
            this.status = status;
        }

    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (!member.getBoards().contains(this)) {
            member.setBoards(this);
        }
    }

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    public void setComment(Comment comment) {
        comments.add(comment);
        if (comment.getBoard() != this) {
            comment.setBoard(this);

        }
    }

    @OneToMany(mappedBy = "board")
    private List<Like> likes = new ArrayList<>();

    public void setLike(Like like) {
        likes.add(like);
        if (like.getBoard() != this) {
            like.setBoard(this);
        }
    }



//    public void removeLike(Like like) {
//        likes.remove(like);
//        if (like.getBoard() == this) {
//            like.removeBoard(this);
//
//        }
   // }
}
