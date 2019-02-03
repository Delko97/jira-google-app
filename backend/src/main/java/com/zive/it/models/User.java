package com.zive.it.models;

import lombok.*;

@Getter
@Setter
public class User {
	int id;
	String username;
	String token;

	public User(String username, String token) {
		this.username = username;
		this.token = token;
	}
}
