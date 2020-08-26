package com.voron.petproject.auth;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.voron.petproject.dao.UserDAO;
import com.voron.petproject.entity.UserEntity;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;

	@Override
	public CustomUserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
		Optional<UserEntity> userEntity = userDAO.findByUsername(username);
		if(userEntity.isPresent()) {
			return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity.get());
		} else {
			throw new UsernameNotFoundException(String.format("User with username: %s not found", username));
		}
	}

}
