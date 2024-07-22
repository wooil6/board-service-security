package com.springboot.like.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.board.service.BoardService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.like.dto.LikeDto;
import com.springboot.like.entity.Like;
import com.springboot.like.mapper.LikeMapper;
import com.springboot.like.repository.LikeRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final LikeMapper likeMapper;


    public LikeService(LikeRepository likeRepository,
                       MemberService memberService,
                       BoardService boardService,
                       BoardRepository boardRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.memberService = memberService;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.likeMapper = likeMapper;
    }

    public void checkLike(LikeDto.Post likeDto) {
        // 회원이 있는지 확인
        // 멤버가 있는지 확인
        Member findMember = memberService.findVerifiedMember(likeDto.getMemberId());
        Board findboard = boardService.findVerifiedBoard(likeDto.getBoardId());
        Optional<Like> optionalLike = likeRepository.findAllByMemberAndBoard(findMember, findboard);

        // like가 있는지 없는지 확인
//        Like findLike= optionalLike.orElseThrow(() ->
//                new BusinessLogicException(ExceptionCode.LIKE_NOT_FOUND));

        // 있으면 기존 like 취소
        if (optionalLike.isPresent()) {
            Like existLike = optionalLike.get();
            likeRepository.deleteById(existLike.getLikeId());
            findboard.setLikeCount(findboard.getLikeCount() - 1);
        }

        // 없으면 추가
        else {
            Like like = likeMapper.likePostDtoToLike(likeDto);
            likeRepository.save(like);
            findboard.setLikeCount(like.getLikeCount() + 1);
        }
        boardRepository.save(findboard);
    }

}
