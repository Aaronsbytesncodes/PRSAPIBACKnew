package com.PRS.Services;

import lombok.Data;

@Data
public class LoginRequest {
    private String Username;
    private String password;
	public String getUsername() {
		
		return Username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUsername(String username) {
		this.Username = username;
	}
}