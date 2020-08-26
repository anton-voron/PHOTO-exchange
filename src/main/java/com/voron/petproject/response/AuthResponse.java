package com.voron.petproject.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voron.petproject.entity.UserEntity;
import com.voron.petproject.jwt.JwtProvider;

@JsonIgnoreProperties(value = "jwtProvider")
public class AuthResponse implements Serializable {


	private JwtProvider jwtProvider = new JwtProvider();

	private String token;
	private Date issueDate;
	private Date expirationDate;
	private UserEntity user;

	public AuthResponse() {
	}

	public AuthResponse(String token, UserEntity user) {
		this.token = token;
		setIssueDate(token);
		setExpirationDate(token);
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String token) {
		issueDate =  jwtProvider.extractIssueDateFromToken(token);
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String token) {
		expirationDate = jwtProvider.extractExpirationDateFromToken(token);
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AuthResponse [jwtProvider=" + jwtProvider + ", token=" + token + ", issueDate=" + issueDate
				+ ", expirationDate=" + expirationDate + ", user=" + user + "]";
	}
	
	

}
