package com.springboot.member.mapper;

import com.springboot.board.entity.Board;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    // postdto를 entity로 저장
    Member meberPostToMember(MemberDto.Post requestBody);

    // patchdto를 entity 로 저장
    Member memberPatchToMember(MemberDto.Patch requestBody);

    // entity를 memberResponse dto로 저장 - 회원 1명
  //  @Mapping(source = "boards", target = "boardList")
    MemberDto.Response memberToMemberResponse(Member member);


    // entity를 memberResponse dto로 저장 - 전체 회원
    List<MemberDto.Response> membersToMemberResponses(List<Member> members);


}