package com.springboot.like.mapper;

import com.springboot.like.dto.LikeDto;
import com.springboot.like.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "boardId", target = "board.boardId")
    Like likePostDtoToLike(LikeDto.Post requestBody);

}

