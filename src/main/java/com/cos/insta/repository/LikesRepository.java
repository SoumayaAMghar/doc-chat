package com.cos.insta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.insta.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer>{

	Likes findByUserIdAndImageId(int userId, int imageId);
	

	int countByImageId(int imageId);
	

	@Query(value="select * from likes where imageId in (select id from image where userId = ?1) order by id desc limit 5;", nativeQuery = true)
	List<Likes> findLikeNotification(int userId);
}
