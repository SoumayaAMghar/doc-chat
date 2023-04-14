package com.cos.insta.controller;

import com.cos.insta.model.Comment;
import com.cos.insta.model.Image;
import com.cos.insta.model.User;
import com.cos.insta.model.dto.CommentRequest;
import com.cos.insta.repository.CommentRepository;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("add")
    public Comment createComment(@RequestBody CommentRequest commentRequest) {
//        return commentRepository.save(comment);
        Comment comment= new Comment();
        comment.setText(commentRequest.getText());

        Image image= new Image();
        image= imageRepository.findById(commentRequest.getImageId()).get();
        comment.setImage(image);

       User user= new User();
//        Optional<User> user= Optional.of(new User());
//        user= userRepository.findById(commentRequest.getCommentOwner());
        if (userRepository.findById(commentRequest.getCommentOwner())!=null){
            user = userRepository.findById(commentRequest.getCommentOwner()).get();
        }

        comment.setCreator(user);
        commentRepository.save(comment);

        System.out.println(commentRequest);
        return comment;
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        return commentRepository.save(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }
}
