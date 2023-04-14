package com.cos.insta.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long Id;
    private String text;

    public Comment(String text) {
        this.text = text;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;
    @ManyToOne
    private Post post;
    @ManyToOne
    @JoinColumn(name="userId")
    private User creator;

    public Comment() {

    }
}
