package com.voron.petproject.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.voron.petproject.entity.UserEntity;

public class CustomUserDetails implements UserDetails {

	private Collection<? extends GrantedAuthority> grantedAuthoritys;
	private String username;
	private String password;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;
	
	
	
	public CustomUserDetails() {
	}

	public static CustomUserDetails fromUserEntityToCustomUserDetails(UserEntity userEntity) {
		CustomUserDetails userDetails = new CustomUserDetails();
		userDetails.username = userEntity.getUsername();
		userDetails.password = userEntity.getPassword();
		userDetails.grantedAuthoritys = new ArrayList<GrantedAuthority>();
		
		return userDetails;
	} 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthoritys;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public String toString() {
		return "CustomUserDetails [grantedAuthoritys=" + grantedAuthoritys + ", username=" + username + ", password="
				+ password + ", isAccountNonExpired=" + isAccountNonExpired + ", isAccountNonLocked="
				+ isAccountNonLocked + ", isCredentialsNonExpired=" + isCredentialsNonExpired + ", isEnabled="
				+ isEnabled + "]";
	}
	
	
}
