package com.cos.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data //lombok
@Entity //JPA -> ORM
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	@JsonIgnore
	private String password;
	private String name;
	private String website;
	private String bio;
	private String email;
	private String phone;
	private String gender;
	private String profileImage;
	
	private String provider; // kakao, google, facebook 
	private String providerId;
	
	// (1) findById() 때만 동작
	// (2) findByUserInfo() 제외
	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties({"user", "tags", "likes"})
	@OrderBy("id desc")
	private List<Image> images = new ArrayList<>();

	@OneToMany(mappedBy = "creator")
	private List<Comment> comments = new ArrayList<>();
	
	@CreationTimestamp // Automatically set the current time
	private Timestamp createDate;
	@CreationTimestamp 
	private Timestamp updateDate;
}
