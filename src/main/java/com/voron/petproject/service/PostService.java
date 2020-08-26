package com.voron.petproject.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.voron.petproject.entity.PostEntity;
import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.response.PostsResponse;

public interface PostService {
	
	public void uploadPostImage(int userId, MultipartFile file, PostEntity post);
	
	public byte[] findById(int postId);

	public void deleteById(int postId);

	public UserEntity findAuthor(int postId);

	public List<byte[]> findAllUsersPost(int userId);
	
	public List<PostsResponse> findAll();
	
	public byte[] downloadUserPostImage(int postId);
}
