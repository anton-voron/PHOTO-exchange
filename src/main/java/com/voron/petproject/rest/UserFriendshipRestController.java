package com.voron.petproject.rest;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.service.FriendService;
import com.voron.petproject.service.UserService;
import com.voron.petproject.utils.UserUtill;

@RestController
@RequestMapping("/api/v1/friends")
public class UserFriendshipRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private FriendService friendService;
	
	@Autowired
	private UserUtill userUtil;
	
	@GetMapping("/")
	public List<UserEntity> findAllFriends(@RequestParam(required = false) Boolean request, @RequestHeader("Authorization") String token) throws UserPrincipalNotFoundException {
		UserEntity user = userUtil.retrieveUserEntity(token);
		if(request) {
			return friendService.findAllFriendsRequest(user.getId());
		}
		return friendService.findAllFriends(user.getId());
	}
	
	
	@PostMapping("/add/{friendId}")
	public UserEntity addNewFriend (@PathVariable int friendId, @RequestHeader("Authorization") String token) throws UserPrincipalNotFoundException {
		UserEntity user = userUtil.retrieveUserEntity(token);
		return friendService.addNewFriend(user.getId(), friendId);
	}
	
	@PutMapping("/confirm/{friendId}")
	public String setFriend (@PathVariable int friendId, @RequestHeader("Authorization") String token) {
		UserEntity user = userUtil.retrieveUserEntity(token);
		friendService.confirmFriend(user.getId(), friendId);
		return "User with id: "  + user.getId() + " successfully add new friend with id " + friendId;
	}
	
	@PutMapping("/delete/{friendId}")
	public String deleteFriend (@PathVariable int friendId, @RequestHeader("Authorization") String token) {
		UserEntity user = userUtil.retrieveUserEntity(token);
		friendService.deleteFriend(user.getId(), friendId);
		return "User with id: "  + user.getId() + " successfully add new friend with id " + friendId;
	}
}
