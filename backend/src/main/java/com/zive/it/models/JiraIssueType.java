package com.zive.it.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JiraIssueType {
	String self;
	String id;
	String description;
	String iconUrl;
	String name;
	String subtask;
	String avatarId;

	public JiraIssueType(String self, String id, String description, String iconUrl, String name, String subtask, String avatarId) {
		this.self = self;
		this.id = id;
		this.description = description;
		this.iconUrl = iconUrl;
		this.name = name;
		this.subtask = subtask;
		this.avatarId = avatarId;
	}

	public JiraIssueType(String self, String id, String description, String iconUrl, String name, String subtask) {
		this.self = self;
		this.id = id;
		this.description = description;
		this.iconUrl = iconUrl;
		this.name = name;
		this.subtask = subtask;
		this.avatarId = null;
	}

	public JiraIssueType() {
	}
}
