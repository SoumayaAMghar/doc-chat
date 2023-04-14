package com.cos.insta.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cos.insta.model.*;
import com.cos.insta.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.LikesRepository;
import com.cos.insta.repository.TagRepository;
import com.cos.insta.service.MyUserDetail;
import com.cos.insta.util.Utils;

@Controller
public class ImageController {

	@Value("C:/users/yc/desktop/insta/src/main/resources/upload/")
	private String fileRealPath;

	@Autowired
	private ImageRepository mImageRepository;
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private TagRepository mTagRepository;

	@Autowired
	private LikesRepository mLikesRepository;

	@GetMapping("/image/explore")
	public String imageExplore(Model model,
			@PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

		// Algorithm (recommended to try in order of the most likes around me)
		Page<Image> pImages = mImageRepository.findAll(pageable);
		List<Image> images = pImages.getContent();

		// 4times likeCount
		for (Image item : images) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}

		model.addAttribute("images", images);
		return "image/explore";
	}

	public class ResourceNotFoundException extends RuntimeException {

		public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
			super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
		}
	}
	@PostMapping("/image/like/{id}")
	public @ResponseBody String imageLike(@PathVariable int id, @AuthenticationPrincipal MyUserDetail userDetail) {

		Likes oldLike = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), id);

		Optional<Image> oImage = mImageRepository.findById(id);
		Image image = oImage.get();

		try {
			if (oldLike == null) { // Unliked status (added)
				Likes newLike = Likes.builder().image(image).user(userDetail.getUser()).build();

				mLikesRepository.save(newLike);
				// Increase like count (modify return value)
				return "like";
			} else { // Liked status (remove)
				mLikesRepository.delete(oldLike);
				// Increase like count (modify return value)
				return "unLike";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fail";
	}
	@PostMapping("/image/{imageId}/comments")
	public String createComment(@PathVariable Long imageId,	 @AuthenticationPrincipal MyUserDetail userDetail , @RequestParam("comment") String commentText) {
		Image image = (Image) mImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
		Comment comment = new Comment(commentText);
		comment.setImage(image);
		commentRepository.save(comment);
		image.getComments().add(comment);
		mImageRepository.save(image);
		return "redirect:/posts/" + image.getId();

	}

	// http://localhost:8083/image/feed/scroll?page=1..2..3..4
	@GetMapping("/image/feed/scroll")
	public @ResponseBody List<Image> imageFeedScroll(@AuthenticationPrincipal MyUserDetail userDetail,
			@PageableDefault( sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {

		Page<Image> pageImages = mImageRepository.findImage(userDetail.getUser().getId(), pageable);
		List<Image> images = pageImages.getContent();

		for (Image image : images) {
			Likes like = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if (like != null) {
				image.setHeart(true);
			}

			// 추가
			int likeCount = mLikesRepository.countByImageId(image.getId());
			image.setLikeCount(likeCount);
		}

		return images;
	}

	@GetMapping("/image/feed")
	public String imageFeed(@AuthenticationPrincipal MyUserDetail userDetail,
			@PageableDefault( sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		// log.info("username : " + userDetail.getUsername());

		// Photos of friends you follow
		Page<Image> pageImages = mImageRepository.findImage(userDetail.getUser().getId(), pageable);

		List<Image> images = pageImages.getContent();

		for (Image image : images) {
			Likes like = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if (like != null) {
				image.setHeart(true);
			}
		}

		// 4번 likeCount
		for (Image item : images) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}

		model.addAttribute("images", images);

		return "image/feed";
	}
	@GetMapping("/" )
	public String imageFeed(@PageableDefault( sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {

		Page<Image> pageImages = mImageRepository.findAll(pageable);
		List<Image> images = pageImages.getContent();

		for (Image item : images) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}

		model.addAttribute("images", images);

		return "image/feed";
	}
	@GetMapping("/image/upload")
	public String imageUpload() {
		return "image/image_upload";
	}

	@PostMapping("/image/uploadProc")
	public String imageUploadProc(@AuthenticationPrincipal MyUserDetail userDetail,
			@RequestParam("file") MultipartFile file, @RequestParam("caption") String caption,
			@RequestParam("location") String location, @RequestParam("tags") String tags) throws IOException {

		// 이미지 업로드 수행
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(fileRealPath + uuidFilename);
		Files.write(filePath, file.getBytes());

		User principal = userDetail.getUser();

		Image image = new Image();
		image.setCaption(caption);
		image.setLocation(location);
		image.setUser(principal);
		image.setPostImage(uuidFilename);

		mImageRepository.save(image);

		List<String> tagList = Utils.tagParser(tags);

		for (String tag : tagList) {
			Tag t = new Tag();
			t.setName(tag);
			t.setImage(image);
			mTagRepository.save(t);
			image.getTags().add(t);
		}
		return "redirect:/";
	}

}
