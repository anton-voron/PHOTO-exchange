package com.voron.petproject.service;

import java.io.InputStream;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	public void uploadUserProfileImage(int userId, MultipartFile file) throws UserPrincipalNotFoundException;

	public void uploadUserPostImage(String path, 
			String fileName, 
			Optional<Map<String, String>> optionalMetadata,
			InputStream inputStream);

	public byte[] downloadUserProfileImage(int userId);

	public byte[] downloadUserPostImage(String path, String key);
	
	public void deleteUserProfileImage(String path, String key);
	
	public void deleteUserPostImage(String path, String key);
	
	@SuppressWarnings("unlikely-arg-type")
	public default boolean isImage(MultipartFile file) {
		if (Arrays.asList(
				ContentType.IMAGE_GIF.getMimeType(), 
				ContentType.IMAGE_JPEG.getMimeType(), 
				ContentType.IMAGE_PNG.getMimeType(), 
				ContentType.IMAGE_SVG.getMimeType()
				)
				.contains(file.getContentType())) {
			return true;
		}
		throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
	}

	public default boolean isFileEmpty(MultipartFile file) {
		if (file.isEmpty()) {
			throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
		}
		return false;
	}

	public default Map<String, String> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}
}
