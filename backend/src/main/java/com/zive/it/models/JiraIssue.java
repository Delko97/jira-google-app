package com.zive.it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

@Getter
@Setter
@ToString
public class JiraIssue {
	String expand;
	String self;
	String id;
	String key;
	@JsonIgnore
	JSONObject fields;

	public JiraIssue(String expand, String self, String id, String key, JSONObject fields) {
		this.expand = expand;
		this.self = self;
		this.id = id;
		this.key = key;
		this.fields = fields;
	}

	public JiraIssue() {
	}
}
