package com.springboot.like.controller;

import com.springboot.like.dto.LikeDto;
import com.springboot.like.entity.Like;
import com.springboot.like.mapper.LikeMapper;
import com.springboot.like.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v11/likes")
@Validated
public class LikeController {
  //  private final static String LIKE_DEFAULT_URI = "/v11/likes";
    private final LikeService likeService;
    private final LikeMapper mapper;


    public LikeController(LikeService likeService,
                          LikeMapper mapper) {
        this.likeService = likeService;
        this.mapper = mapper;
    }

    @PostMapping
    public void postLike(
            @Valid@RequestBody LikeDto.Post requestBody) {
        Like like = mapper.likePostDtoToLike(requestBody);
        likeService.checkLike(requestBody);
    }
}
