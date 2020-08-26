package com.voron.petproject.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voron.petproject.dao.FriendDAO;
import com.voron.petproject.dao.UserDAO;
import com.voron.petproject.entity.FriendEntity;
import com.voron.petproject.entity.UserEntity;

@Service
public class FriendServiceImpl implements FriendService {

	@Autowired
	private FriendDAO friendDAO;

	@Autowired
	private UserDAO userDAO;

	private UserEntity validateUserId(int userId) throws UserPrincipalNotFoundException {
		Optional<UserEntity> userResponse = userDAO.findById(userId);
		if (userResponse.isPresent()) {
			return userResponse.get();
		} else {
			throw new UserPrincipalNotFoundException("User with id: " + userId + " was not found");
		}
	}

	@Transactional
	@Override
	public List<UserEntity> findAllFriends(int userId) throws UserPrincipalNotFoundException {
		UserEntity user = validateUserId(userId);
		
		List<FriendEntity> usersFriends = friendDAO.findAllFriends(userId);
		List<UserEntity> friends = usersFriends.stream()
				.map(friendship -> {
					user.addNewFriend(friendship);
					return friendship.getFriend();
				}).collect(Collectors.toList());

		return friends;
	}

	@Transactional
	@Override
	public List<UserEntity> findAllFriendsRequest(int userId) throws UserPrincipalNotFoundException {
		UserEntity user = validateUserId(userId);

		List<FriendEntity> usersFriends = friendDAO.findAllFriendsRequest(userId);
		
		List<UserEntity> friends = usersFriends.stream()
				.map(friendship -> {
					user.addNewFriend(friendship);
					return friendship.getFriend();
				}).collect(Collectors.toList());
		
		return friends;
	}

	@Transactional
	@Override
	public UserEntity addNewFriend(int userId, int friendId) throws UserPrincipalNotFoundException {

		UserEntity user = validateUserId(userId);
		UserEntity friend = validateUserId(friendId);

		FriendEntity friendship = new FriendEntity(user, friend, 0);
		friendDAO.addFriend(userId, friendId);
		return friend;
	}
	
	@Transactional
	@Override
	public void confirmFriend(int userId, int friendId) {
		try {
			UserEntity user = validateUserId(userId);
			UserEntity friend = validateUserId(friendId);
		} catch (UserPrincipalNotFoundException e) {
			e.printStackTrace();
		}
		friendDAO.confirmFriend(userId, friendId);
	}

	@Transactional
	@Override
	public void deleteFriend(int userId, int friendId) {
		try {
			UserEntity user = validateUserId(userId);
			UserEntity friend = validateUserId(friendId);
		} catch (UserPrincipalNotFoundException e) {
			e.printStackTrace();
		}
		friendDAO.deleteFriend(userId, friendId);
	}

}
