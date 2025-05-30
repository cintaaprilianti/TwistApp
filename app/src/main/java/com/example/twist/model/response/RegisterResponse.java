package com.example.twist.model.response;

public class RegisterResponse{
	private User user;
	private String token;

	public User getUser(){
		return user;
	}

	public String getToken(){
		return token;
	}
}
