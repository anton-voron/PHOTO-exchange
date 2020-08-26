package com.voron.petproject.dao;

import com.voron.petproject.entity.UserEntity;
import java.util.Optional;

public interface CustomUserDAO {
	
	public Optional<UserEntity> findByUsername(String username);
}
