package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByMember(Member member);

    Page<Board> findByMember_MemberId(long memberId, Pageable pageable);

   // Comment findByCommentId(long commentId);


}
