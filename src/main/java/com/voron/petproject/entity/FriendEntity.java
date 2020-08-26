package com.voron.petproject.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "friends")
public class FriendEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int userId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserEntity user;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "friend_id", referencedColumnName = "id")
	private UserEntity friend;
	
	@Column(name = "status")
	private int status;
	
	
	public FriendEntity() {}
	

	public FriendEntity(UserEntity user, UserEntity friend, int status) {
		this.user = user;
		this.friend = friend;
		this.status = status;
	}



	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public UserEntity getFriend() {
		return friend;
	}

	public void setFriend(UserEntity friend) {
		this.friend = friend;
	}

	
	
}
