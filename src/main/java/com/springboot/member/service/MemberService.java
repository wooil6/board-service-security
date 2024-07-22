package com.springboot.member.service;

import com.springboot.auth.utils.CustomAuthorityUtils;
import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.helper.event.MemberRegistrationApplicationEvent;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public MemberService(MemberRepository memberRepository,
                         ApplicationEventPublisher publisher,
                         PasswordEncoder passwordEncoder,
                         CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.publisher = publisher;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }


//    @PostConstruct
//    public void init() {
//        this.boardService = boardService;
//    }

    // 멤버 등록
    public Member createMember(Member member) {
        // 존재하는 멤버인지 확인
        verifyExistsEmail(member.getEmail());

        //password 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        //DB에 User Role 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member saveMember = memberRepository.save(member);

        publisher.publishEvent(new MemberRegistrationApplicationEvent(this, saveMember));

        return saveMember;
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }


    // 멤버 탈퇴
    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);
       // boardService.deactivateBoardsByMember(memberId);
        findMember.getBoards().stream()
                        .forEach(board -> {
                            board.setQuestionStatus(Board.QuestionStatus.QUESTION_DEACTIVED);
                            board.setModifiedAt(LocalDateTime.now());
                        });
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);

        memberRepository.save(findMember);
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(()
                -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        return findMember;
    }
}