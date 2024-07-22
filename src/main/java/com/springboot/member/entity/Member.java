package com.springboot.member.entity;

import com.springboot.comment.entity.Comment;
import com.springboot.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 13)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태"),
        ;

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<Board> boards = new ArrayList<>();

    public void setBoards(Board board) {
        boards.add(board);
        if (board.getMember() != this) {
            board.setMember(this);
        }
    }


    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    public void setComment(Comment comment) {
        comments.add(comment);
        if (comment.getMember() != this) {
            comment.setMember(this);
        }
    }

}