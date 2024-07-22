//package com.springboot.member.service;
//
//import com.springboot.auth.utils.CustomAuthorityUtils;
//import com.springboot.exception.BusinessLogicException;
//import com.springboot.exception.ExceptionCode;
//import com.springboot.helper.event.MemberRegistrationApplicationEvent;
//import com.springboot.member.entity.Member;
//import com.springboot.member.repository.MemberRepository;
//import org.apache.catalina.core.ApplicationPushBuilder;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Transactional
//@Service
//public class MemberService {
//    private final MemberRepository memberRepository;
//    private final ApplicationEventPublisher publisher;
//    private final PasswordEncoder passwordEncoder;
//    private CustomAuthorityUtils authorityUtils;
//
//    public MemberService(MemberRepository memberRepository,
//                         ApplicationEventPublisher publisher,
//                         PasswordEncoder passwordEncoder,
//                         CustomAuthorityUtils authorityUtils) {
//        this.memberRepository = memberRepository;
//        this.publisher = publisher;
//        this.passwordEncoder = passwordEncoder;
//        this.authorityUtils = authorityUtils;
//    }
//
//    public Member createMember(Member member) {
//        verifyExistsEmail(member.getEmail());
//
//        // 패스워드 암호화
//        String encryptedPassword = passwordEncoder.encode(member.getPassword());
//        member.setPassword(encryptedPassword);
//
//        // 데이터베이스에 user Role 저장
//        List<String> roles = authorityUtils.createRoles(member.getEmail());
//        member.setRoles(roles);
//
//        Member saveMember = memberRepository.save(member);
//
//        publisher.publishEvent(new MemberRegistrationApplicationEvent(this, saveMember));
//        return saveMember;
//    }
//
////    @Transactional
////    public Member updateMember(Member member) {
////        Member findMember = findVerifiedMember(member.getMemberId());
////    }
////
//    public void verifyExistsEmail(String email) {
//        Optional<Member> member = memberRepository.findByEmail(email);
//        if (member.isPresent()) {
//            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
//        }
//    }
//
//    @Transactional
//    public Member findVerifiedMember(long memberId) {
//        Optional<Member> optionalMember = memberRepository.findById(memberId);
//        Member findMember = optionalMember.orElseThrow(() ->
//                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        return findMember;
//    }
//}
