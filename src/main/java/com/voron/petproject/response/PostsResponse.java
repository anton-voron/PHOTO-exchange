package com.voron.petproject.response;

import java.io.Serializable;
import java.util.Arrays;

import com.voron.petproject.entity.PostEntity;

public class PostsResponse implements Serializable {
	private PostEntity postEntity;
	private String postImage;
	
	public PostsResponse() {
	}
	
	public PostsResponse(PostEntity postEntity, String postImage) {
		this.postEntity = postEntity;
		this.postImage = postImage;
	}
	public PostEntity getPostEntity() {
		return postEntity;
	}
	public void setPostEntity(PostEntity postEntity) {
		this.postEntity = postEntity;
	}
	public String getPostImage() {
		return postImage;
	}
	public void setPostImage(String postImage) {
		this.postImage = postImage;
	}
	
}
