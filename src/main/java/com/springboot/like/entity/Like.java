package com.springboot.like.entity;

import com.springboot.board.entity.Board;
import com.springboot.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "LIKES")
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @Column(nullable = false)
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
        if (!board.getLikes().contains(this)) {
            board.setLike(this);
        }
    }

//    public void removeBoard(Board board) {
//        this.board = board;
//        if (board.getLikes().contains(this)) {
//            board.removeLike(this);
//        }
//    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
