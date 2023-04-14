package com.cos.insta.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequest {

    private String text;
    private Integer commentOwner;
    private Integer imageId;

}
