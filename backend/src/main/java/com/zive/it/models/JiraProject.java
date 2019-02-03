package com.zive.it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JiraProject {
	String expand;
	String self;
	Long id;
	String key;
	String name;
	@JsonIgnore
	JsonObject avatarUrls;
	String projectTypeKey;

	public JiraProject(String expand, String self, Long id, String key, String name, JsonObject avatarUrls, String projectTypeKey) {
		this.expand = expand;
		this.self = self;
		this.id = id;
		this.key = key;
		this.name = name;
		this.avatarUrls = null;
		this.projectTypeKey = projectTypeKey;
	}

	public JiraProject() {
	}
}
