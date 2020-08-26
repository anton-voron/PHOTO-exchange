package com.voron.petproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.voron.petproject.entity.FriendEntity;
import com.voron.petproject.entity.UserEntity;

public interface FriendDAO extends JpaRepository<FriendEntity, Integer> {
	
	@Modifying
	@Query(
			value = "INSERT INTO `friends` (`user_id`, `friend_id`) values (:user_id, :friend_id)",
			nativeQuery = true)
	public void addFriend(@Param("user_id")int user_id, @Param("friend_id")int friend_id);
	
	
	@Query(
			value = "SELECT * FROM friends WHERE (`user_id` =:user_id ) AND (`status` = '1')",
			nativeQuery = true)
	public List<FriendEntity> findAllFriends(@Param("user_id") int user_id);
	
	@Modifying
	@Query(
			value = "UPDATE `friends` SET `status` = '1' WHERE (`user_id` =:friend_id) and (`friend_id` =:user_id)",
			nativeQuery = true)
	public void confirmFriend(@Param("user_id")int user_id, @Param("friend_id")int friend_id);
	
	@Modifying
	@Query(
			value = "UPDATE `friends` SET `status` = '2' WHERE (`user_id` =:friend_id) and (`friend_id` =:user_id)",
			nativeQuery = true)
	public void deleteFriend(@Param("user_id")int user_id, @Param("friend_id")int friend_id);

	@Query(
			value = "SELECT * FROM friends WHERE (`user_id` =:user_id) AND (`status` = '0')",
			nativeQuery = true)
	public List<FriendEntity> findAllFriendsRequest(@Param("user_id") int user_id);

}
