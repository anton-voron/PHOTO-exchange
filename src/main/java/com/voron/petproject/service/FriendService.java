package com.voron.petproject.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import com.voron.petproject.entity.UserEntity;

public interface FriendService {
	public List<UserEntity> findAllFriends(int userId) throws UserPrincipalNotFoundException ;
	
	public List<UserEntity> findAllFriendsRequest(int userId) throws UserPrincipalNotFoundException ;
	
	public UserEntity addNewFriend(int userId, int friendId) throws UserPrincipalNotFoundException;

	public void confirmFriend(int userId, int friendId);

	public void deleteFriend(int userId, int friendId);
}
