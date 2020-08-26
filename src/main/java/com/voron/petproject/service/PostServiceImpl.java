package com.voron.petproject.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.cloudsearchdomain.model.Bucket;
import com.voron.petproject.amazon_bucket.BucketName;
import com.voron.petproject.dao.PostDAO;
import com.voron.petproject.entity.PostEntity;
import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.response.PostsResponse;

@Service
public class PostServiceImpl implements PostService {

	private final FileStorageService fileStorage;
	private final UserService userService;
	private final PostDAO postDAO;

	@Autowired
	public PostServiceImpl(FileStorageService fileStorage, UserService userService, PostDAO postDAO) {
		this.fileStorage = fileStorage;
		this.userService = userService;
		this.postDAO = postDAO;
	}

	@Override
	public void uploadPostImage(int userId, MultipartFile file, PostEntity post) {
		// 1. Check if image is not empty
		fileStorage.isFileEmpty(file);
		// 2. If file is an image
		fileStorage.isImage(file);

		// 3. The user exists in our database
		Optional<UserEntity> userRequest = userService.findById(userId);
		if (userRequest.isPresent()) {
			UserEntity user = userRequest.get();
			post.setAuthor(user);

			String path = String.format("%s/%s-%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId(),
					file.getOriginalFilename());
			post.setPostPath(path);

			String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
			post.setFileName(fileName);

			Map<String, String> metadata = fileStorage.extractMetadata(file);

			try {
				postDAO.save(post);
				fileStorage.uploadUserPostImage(path, fileName, Optional.of(metadata), file.getInputStream());
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		} else {
			throw new IllegalStateException("User with id: " + userId + " not found");
		}

	}

	@Override
	public byte[] findById(int postId) {
		PostEntity post = postDAO.findById(postId).get();
		return fileStorage.downloadUserPostImage(post.getPostPath(), post.getFileName());
	}

	@Override
	public void deleteById(int postId) {
		PostEntity post = postDAO.findById(postId).get();
		fileStorage.deleteUserPostImage(post.getPostPath(), post.getFileName());
		postDAO.delete(post);
	}

	@Override
	public UserEntity findAuthor(int postId) {
		PostEntity post = postDAO.findById(postId).get();
		UserEntity user = post.getAuthor();
		return user;
	}

	@Override
	public List<byte[]> findAllUsersPost(int userId) {
		List<PostEntity> posts = postDAO.findAllUsersPost(userId);
		List<byte[]> resultList = posts.stream().map(post -> fileStorage.downloadUserPostImage(post.getPostPath(), post.getFileName())).collect(Collectors.toList());
		return resultList;
	}

	@Override
	public List<PostsResponse> findAll() {
		List<PostEntity> posts = postDAO.findAll();
		
		List<PostsResponse> resultList = posts.stream().map(post -> (
					new PostsResponse(post, String.format("http://localhost:8000/api/v1/profile/%d/post/download", post.getId()))
				)).collect(Collectors.toList());
		return resultList;
	}

	@Override
	public byte[] downloadUserPostImage(int postId) {
		PostEntity post = postDAO.findById(postId).get();
		return fileStorage.downloadUserPostImage(post.getPostPath(), post.getFileName());
	}

}
