package com.voron.petproject.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.voron.petproject.dao.UserDAO;
import com.voron.petproject.entity.UserEntity;

import java.util.regex.Matcher;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private UserDAO userDAO;
	private AmazonS3 s3;

	@Autowired
	public FileStorageServiceImpl(UserDAO userDAO, AmazonS3 s3) {
		this.userDAO = userDAO;
		this.s3 = s3;
	}

	private UserEntity validateUserId(int userId) throws UserPrincipalNotFoundException {
		Optional<UserEntity> userResponse = userDAO.findById(userId);
		if (userResponse.isPresent()) {
			return userResponse.get();
		} else {
			throw new UserPrincipalNotFoundException("User with id: " + userId + " was not found");
		}
	}

	private void createFileImageOnDesktop(String path, MultipartFile file) {
		Pattern pattern = Pattern.compile("^\\w*\\/");
		Matcher matcher = pattern.matcher(file.getContentType());
		String contentType = matcher.replaceAll("");
		File imageFile = new File(path + "/" + file.getName() + "." + contentType);
		if (imageFile.exists())
			return;
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile))) {
			bos.write(file.getBytes());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void uploadUserProfileImage(int userId, MultipartFile file) throws UserPrincipalNotFoundException {
		isFileEmpty(file);
		// 2. If file is an image
		isImage(file);

		// 3. The user exists in our database
		UserEntity user = validateUserId(userId);

		// 4. Grab some metadata from file if any
		Map<String, String> metadata = extractMetadata(file);

		String pathToDirString = "/Users/a4085/Desktop/Java Projects/Images";
		File imagedDir = new File(pathToDirString);
		if (!imagedDir.exists()) {
			if (imagedDir.mkdir()) {
				System.out.print("Directory was created");
				createFileImageOnDesktop(pathToDirString, file);
			}
		} else {
			System.out.print("Directory was already created");
			createFileImageOnDesktop(pathToDirString, file);
		}

		try {
			user.setProfilePic(file.getBytes());
			userDAO.save(user);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Transactional
	@Override
	public byte[] downloadUserProfileImage(int userId) {
		Blob sqlFile = userDAO.downloadUserProfileImage(userId);
		byte[] buffer = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				BufferedInputStream bis = new BufferedInputStream(sqlFile.getBinaryStream())) {

			buffer = new byte[bis.available()];
			bis.read(buffer, 0, buffer.length);
			return buffer;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	@Override
	public void uploadUserPostImage(String path, String fileName, Optional<Map<String, String>> optionalMetadata,
			InputStream inputStream) {

		ObjectMetadata metadata = new ObjectMetadata();
		optionalMetadata.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(metadata::addUserMetadata);
			}
		});
		try {
			s3.putObject(path, fileName, inputStream, metadata);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException("Failed to store file to s3", e);
		}
	}

	@Override
	public byte[] downloadUserPostImage(String path, String key) {
		try {
			S3Object object = s3.getObject(path, key);
			S3ObjectInputStream inputStream = object.getObjectContent();
			return new BufferedInputStream(inputStream).readAllBytes();
		} catch (AmazonServiceException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void deleteUserProfileImage(String path, String key) {
		try {
			s3.deleteObject(path, key);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void deleteUserPostImage(String path, String key) {
		try {
			s3.deleteObject(path, key);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException(e);
		}

	}

}
