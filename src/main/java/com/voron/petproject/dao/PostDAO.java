package com.voron.petproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.voron.petproject.entity.PostEntity;

public interface PostDAO extends JpaRepository<PostEntity, Integer> {
	
	@Query(value = "SELECT * from posts where user_id=:userId", nativeQuery = true)
	public List<PostEntity> findAllUsersPost(@Param("userId") int userId);
	
//	@Query(value = "SELECT * from posts", nativeQuery = true)
//	public List<PostEntity> findAll();
}
