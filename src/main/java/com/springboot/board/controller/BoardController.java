package com.springboot.board.controller;

import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import com.springboot.board.mapper.BoardMapper;
import com.springboot.board.service.BoardService;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.service.CommentService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.entity.Member;
import com.springboot.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v11/boards")
@Validated
public class BoardController {
    private final static String BOARD_DEFAULT_URL = "/v11/boards";
    private final BoardService boardService;
    private final BoardMapper mapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public BoardController(BoardService boardService, BoardMapper mapper,
                           CommentService commentService, CommentMapper commentMapper) {
        this.boardService = boardService;
        this.mapper = mapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public ResponseEntity postBoard(@Valid @RequestBody BoardDto.Post requestBody) {
        Board board = mapper.boardPostDtoToBoard(requestBody);

        Board createBoard = boardService.createBoard(board);
        URI location = UriCreator.createUri(BOARD_DEFAULT_URL, createBoard.getBoardId());

        return ResponseEntity.created(location).build();

    }

    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(
            @PathVariable("board-id") @Positive long boardId,
            @Valid @RequestBody BoardDto.Patch requestBody) {

        requestBody.setBoardId(boardId);

        Board board = boardService.updateBoard(mapper.boardPatchDtoToBoard(requestBody));
       // board.getBoardStatus() = commentService.setCommentStatus(board);
      //  Board.BoardStatus commentStatus = commentService.setCommentStatus(board);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.OK);
    }

    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(
            @PathVariable("board-id") @Positive long boardId) {

        Board board = boardService.findBoard(boardId);
     //   board.setView(board.getView()+1);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.OK);
    }

    @GetMapping("/members/{member-id}")
    public ResponseEntity getBoardsMembers(
            @PathVariable("member-id") @Positive long memberId,
            @Positive @RequestParam int page, @Positive @RequestParam int size) {

        Page<Board> pageBoards = boardService.findMemberBoards(memberId, page - 1, size);
        List<Board> boards = pageBoards.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.boardsToBoardResponseDtos(boards), pageBoards), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity getBoards(@Positive @RequestParam int page, @Positive @RequestParam int size) {
//        Page<Board> pageBoards = boardService.findBoards(page - 1, size);
//        List<Board> boards = pageBoards.getContent();
//        return new ResponseEntity<>(new MultiResponseDto<>(
//                mapper.boardsToBoardResponseDtos(boards), pageBoards), HttpStatus.OK);
//    }

//    @GetMapping("/createAt")
//    public ResponseEntity getBoardsCreateAt(@PathVariable("createAt")
//                                            @Positive @RequestParam int page, @Positive @RequestParam int size
//                                            //@RequestParam("createAt") LocalDateTime createAt
//    ) {
//        Page<Board> pageBoards = boardService.findBoardsCreateAT(page - 1, size);
//        List<Board> boards = pageBoards.getContent();
//        return new ResponseEntity<>(new MultiResponseDto<>(
//                mapper.boardsToBoardResponseDtos(boards), pageBoards), HttpStatus.OK);
//    }
        @GetMapping
        public ResponseEntity getBoards(@Positive @RequestParam int page, @Positive @RequestParam int size,
                                        @RequestParam String sort) {
            // sort를 _로 분할해서 0번 인덱스를 가져옴
            // sort가 boardid_asc -> boardid를 가져옴
            // 분할된 부분으로 오름차순 정렬 객체를 생성
            Sort sortOrder = Sort.by(sort.split("_")[0]).ascending();
            // _로 분할해서 1번 인덱스를 가져옴
            // 두번째 부분이 desc인지 확인(대소문자 구분X)
            if (sort.split("_")[1].equalsIgnoreCase("desc")) {
                sortOrder = sortOrder.descending();
            }

            Page<Board> pageBoards = boardService.findBoardsSort(page - 1, size, sortOrder);
            List<Board> boards = pageBoards.getContent();
            return new ResponseEntity<>(new MultiResponseDto<>(
                    mapper.boardsToBoardResponseDtos(boards), pageBoards), HttpStatus.OK);
        }





    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") @Positive long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

