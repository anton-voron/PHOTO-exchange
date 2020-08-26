package com.voron.petproject.rest;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.Response;
import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.jwt.JwtProvider;
import com.voron.petproject.request.AuthRequest;
import com.voron.petproject.response.AuthResponse;
import com.voron.petproject.service.UserService;
import com.voron.petproject.utils.UserUtill;

@RestController
@RequestMapping("/api/v1/")
public class UserAuthRestController {

	private final UserService userService;
	private JwtProvider jwtProvider;
	private UserUtill userUtil;

	@Autowired
	public UserAuthRestController(UserService userService, JwtProvider jwtProvider, UserUtill userUtil) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
		this.userUtil = userUtil;
	}

	@PostMapping("/registration")
	public UserEntity registration(@RequestBody UserEntity user) {
		try {
			return userService.save(user);
		} catch (Exception e) {
			throw new RuntimeException("On the server was cached error " + e.getMessage());
		}
	}

	@GetMapping("/users")
	public List<UserEntity> findAll() {
		return userService.findAll();
	}

	@DeleteMapping("/delete")
	public UserEntity deleteById(@RequestHeader("Authorization") String token) throws UserPrincipalNotFoundException {
		UserEntity user = userUtil.retrieveUserEntity(token);
		return userService.deleteById(user.getId());
	}

	@GetMapping("/myprofile")
	public UserEntity findById(@RequestHeader("Authorization") String token) throws UserPrincipalNotFoundException {
		UserEntity user = userUtil.retrieveUserEntity(token);
		return userService.findById(user.getId()).get();
	}

	@PostMapping("/login")
	public AuthResponse login(@RequestBody AuthRequest user) {
		UserEntity userEntity  = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
		String token = jwtProvider.generateToken(userEntity.getUsername(), userEntity.getId());
		return new AuthResponse(token, userEntity);
	}
}
