 package com.voron.petproject.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.voron.petproject.entity.PostEntity;
import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.response.PostsResponse;
import com.voron.petproject.service.FileStorageService;
import com.voron.petproject.service.PostService;
import com.voron.petproject.utils.UserUtill;

@RestController
@RequestMapping("/api/v1/profile")
public class MediaPostRestController {

	private final FileStorageService fileStorageService;
	private final PostService postService;
	private final UserUtill userUtil;
	
	
	
	

	public MediaPostRestController(FileStorageService fileStorageService,
			PostService postService, UserUtill userUtil) {
		this.fileStorageService = fileStorageService;
		this.postService = postService;
		this.userUtil = userUtil;
	}

	@PostMapping(
			path = "/image/upload",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Content-Type=multipart/form-data"
			)
	public void uploadUserProfileImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
		UserEntity user = userUtil.retrieveUserEntity(token);
		try {
			fileStorageService.uploadUserProfileImage(user.getId(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@PostMapping(
			path = "/post/upload",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public String uploadUserPostImage(
			@RequestParam(name = "file", required = false) MultipartFile file, 
			@RequestHeader(value = "Authorization", required=false) String token, 
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="geolocation", required=false) String geolocation
			) {
		System.out.println("\n\n======== uploadUserPostImage");
		System.out.println(token);
		System.out.println(file);
		System.out.println(description);
		System.out.println(geolocation);
		PostEntity post  = new PostEntity(description, geolocation);
		UserEntity user = userUtil.retrieveUserEntity(token);
		postService.uploadPostImage(user.getId(), file, post);
		return "Sucess";
	}
	
	
	@GetMapping(path = "/image/download")
	public byte[] downloadUserProfileImage(@RequestHeader("Authorization") String token) {
		UserEntity user = userUtil.retrieveUserEntity(token);
		return fileStorageService.downloadUserProfileImage(user.getId());
	}
	
	
	@GetMapping("{userId}/posts")
	public List<byte[]> findAllUsersPost(@PathVariable("userId") int userId, @RequestHeader("Authorization") String token) {
		UserEntity user = userUtil.retrieveUserEntity(token);
		if(user == null) {
			throw new IllegalStateException("Bad authorization request");
		} else {
			return postService.findAllUsersPost(userId);			
		}
	}
	
	
	@GetMapping("posts")
	public List<PostsResponse> findAllPosts() {
		return postService.findAll();
	}
	
	@GetMapping("{postId}/post/download")
	public byte[] downloadUserPostImage (@PathVariable("postId") int postId) {
		return postService.downloadUserPostImage(postId);
	}
	
}
