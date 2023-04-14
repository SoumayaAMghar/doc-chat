package com.cos.insta.repository;

import com.cos.insta.model.Comment;
import com.cos.insta.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(Long postId);
//    Comment findByUserIdAndImageId(int userId, int imageId);
}
