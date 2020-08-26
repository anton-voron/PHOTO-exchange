package com.voron.petproject.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import com.voron.petproject.entity.UserEntity;

public interface UserService {

	public Optional<UserEntity> findByUsername(String username);

	public UserEntity findByUsernameAndPassword(String username, String password);

	public Optional<UserEntity> findById(int userId);

	public UserEntity save(UserEntity user);

	public void update(UserEntity user);

	public UserEntity deleteById(int userId) throws UserPrincipalNotFoundException;

	public List<UserEntity> findAll();
	
}
