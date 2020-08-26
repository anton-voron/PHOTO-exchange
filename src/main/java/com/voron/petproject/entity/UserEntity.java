package com.voron.petproject.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = {"friends", "posts"})
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column(name = "friend_count")
	private int friendCount;

	@Column(name = "profile_pic")
	private byte[] profilePic;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<PostEntity> posts = new HashSet<PostEntity>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "friend")
	@JsonBackReference
	private Set<FriendEntity> friends = new HashSet<FriendEntity>();

	public UserEntity() {
	}

	public UserEntity(String username, String firstName, String lastName, String email, String password,
			int friendCount, byte[] profilePic, Set<PostEntity> posts, Set<FriendEntity> friends) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.friendCount = friendCount;
		this.profilePic = profilePic;
		this.posts = posts;
		this.friends = friends;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public Set<PostEntity> getPosts() {
		return posts;
	}

	public void setPosts(Set<PostEntity> posts) {
		this.posts = posts;
	}

	public Set<FriendEntity> getFriends() {
		return friends;
	}

	public void setFriends(Set<FriendEntity> friends) {
		this.friends = friends;
	}

	public boolean addNewFriend(FriendEntity friend) {
		return friends.add(friend);
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", password=" + password + ", friendCount=" + friendCount + ", posts=" + posts
				+ ", friends=" + friends + "]";
	}
	
	
}
