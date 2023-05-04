package com.project.travel.payload.response;

import com.project.travel.models.Interest;
import org.springframework.http.ResponseCookie;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class UserInfoResponse implements Serializable {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private Set<Interest> interests;
	private ResponseCookie cookie;

	public UserInfoResponse(Long id, String username, String email, List<String> roles, Set<Interest> interests, ResponseCookie cookie) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.interests = interests;
		this.cookie = cookie;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public Set<Interest> getInterests() {
		return interests;
	}
}
