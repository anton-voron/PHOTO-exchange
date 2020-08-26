package com.voron.petproject.dao;

import java.sql.Blob;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.voron.petproject.entity.UserEntity;

@Repository
public interface UserDAO extends JpaRepository<UserEntity, Integer>, CustomUserDAO {
	
//	@Query(value = "SELECT * FROM users u WHERE u.username =:username", nativeQuery = true)
//	public Optional<UserEntity>  findByUsername(@Param("username") String username);
	 
	@Query(value = "SELECT profile_pic FROM users WHERE id=:userId", nativeQuery = true)
	public Blob downloadUserProfileImage(@Param("userId") int userId);
}
