package com.zive.it.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JiraUser {
	String key;
	String name;
	String displayName;
	Boolean active;
	String emailAddress;
	String accountId;
	String avatar;
}
