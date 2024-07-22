package com.springboot.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class LikeDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private long memberId;
        private long boardId;
    }
//    public static class Response {
//        private long likeId;
//        private long boardId;
//        private long memberId;
//    }
}
