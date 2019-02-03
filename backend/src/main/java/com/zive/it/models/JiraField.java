package com.zive.it.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JiraField {
	String id;
	String type;
	String value;
	List<String> allowedValues;
}
