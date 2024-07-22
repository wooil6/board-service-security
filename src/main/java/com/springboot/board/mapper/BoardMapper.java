package com.springboot.board.mapper;

import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import com.springboot.comment.mapper.CommentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface BoardMapper {
    @Mapping(source = "memberId", target = "member.memberId")
    Board boardPostDtoToBoard(BoardDto.Post requestBody);

    Board boardPatchDtoToBoard(BoardDto.Patch requestBody);

    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(target = "comments", qualifiedByName = "commentsToCommentResponses")
    BoardDto.Response boardToBoardResponseDto(Board board);

    List<BoardDto.Response> boardsToBoardResponseDtos(List<Board> boards);

    List<BoardDto.Response> boardsToBoardResponseDtosCreateAt(List<Board> boards);
    }
