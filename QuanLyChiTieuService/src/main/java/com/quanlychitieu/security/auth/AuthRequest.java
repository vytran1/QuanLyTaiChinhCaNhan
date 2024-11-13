package com.quanlychitieu.security.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public class AuthRequest {
	@Length(min = 5, max = 50)
	@NotNull
	private String username;

	@Length(min = 5, max = 50)
	@NotNull
	private String password;

	public AuthRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
