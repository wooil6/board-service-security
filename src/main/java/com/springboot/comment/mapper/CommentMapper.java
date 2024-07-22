package com.springboot.comment.mapper;

import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "boardId", target = "board.boardId")
    Comment commentPostDtoToComment(CommentDto.Post requestBody);

    Comment commentPatchDtoToComment(CommentDto.Patch requestBody);

    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "board.boardId", target = "boardId")
    @Mapping(source = "commentStatus", target = "commentStatus")
    CommentDto.Response commentToCommentResponse(Comment comment);

    @Named("commentsToCommentResponses")
    List<CommentDto.Response> commentsToCommentResponses(List<Comment> comments);

}

