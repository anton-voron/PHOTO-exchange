package com.voron.petproject.utils;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.jwt.JwtProvider;
import com.voron.petproject.service.UserService;
import static org.springframework.util.StringUtils.hasText;

@Component
public class UserUtill {

	private  UserService userService;
	private  JwtProvider jwtProvider;

	@Autowired
	public UserUtill(UserService userService, JwtProvider jwtProvider) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
	}

	@Transactional
	public  UserEntity retrieveUserEntity(String token) {
		String bearer = "Bearer ";
		String jwtToken = null;
		UserEntity user = null;
		if(hasText(token) && token.startsWith(bearer)) {
			jwtToken = token.substring(bearer.length());			
			if(jwtProvider.validateToken(jwtToken)) {				
				int userId = jwtProvider.getUserIdFromToken(jwtToken);
				user = userService.findById(userId).get();
			}
		}

	
		return user;
	}
}
