package com.voron.petproject.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.voron.petproject.dao.FriendDAO;
import com.voron.petproject.dao.UserDAO;
import com.voron.petproject.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private FriendDAO friendDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public Optional<UserEntity> findByUsername(String username) {
		Optional<UserEntity> user = userDAO.findByUsername(username);
		return user;
	}

	@Transactional
	@Override
	public UserEntity findByUsernameAndPassword(String username, String password) {
		Optional<UserEntity> userReqeust = userDAO.findByUsername(username);
		UserEntity user = null;
		if (userReqeust.isPresent()) {
			user = userReqeust.get();
			if (passwordEncoder.matches(password, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	@Transactional
	@Override
	public Optional<UserEntity> findById(int userId) {
		Optional<UserEntity> user = userDAO.findById(userId);
		return user;
	}

	@Override
	public UserEntity save(UserEntity user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userDAO.save(user);
	}

	@Transactional
	@Override
	public void update(UserEntity user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDAO.save(user);
	}

	@Transactional
	@Override
	public UserEntity deleteById(int userId) throws UserPrincipalNotFoundException {
		Optional<UserEntity> user = userDAO.findById(userId);

		if (!user.isPresent())
			throw new UserPrincipalNotFoundException("User with id: " + userId + " not exist");
		userDAO.delete(user.get());
		return user.get();
	}

	@Transactional
	@Override
	public List<UserEntity> findAll() {
		return userDAO.findAll();
	}

}
