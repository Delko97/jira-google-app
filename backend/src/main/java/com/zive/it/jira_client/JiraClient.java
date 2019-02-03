package com.zive.it.jira_client;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.binding.StringFormatter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;
import com.zive.it.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JiraClient {

	private String auth = new String(Base64.encode("dominik.dujava@student.tuke.sk:126domino126"));

	private String url = "https://ziveit.atlassian.net";

	private JsonParser parser = new JsonParser();

	public List<JiraUser> getAllUsers() {
		Client client = Client.create();
		WebResource webResource = client.resource("https://ziveit.atlassian.net/rest/api/2/user/search?username=%25");

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		if (response.getStatus() == 200) {
			JsonArray array = parser.parse(response.getEntity(String.class)).getAsJsonArray();
			List<JiraUser> users = new ArrayList<>();
			array.forEach(x -> users.add(new JiraUser(x.getAsJsonObject().get("key").getAsString()
					, x.getAsJsonObject().get("name").getAsString(), x.getAsJsonObject().get("displayName").getAsString()
					, x.getAsJsonObject().get("active").getAsBoolean(), x.getAsJsonObject().get("emailAddress").getAsString()
					, x.getAsJsonObject().get("accountId").getAsString()
					, x.getAsJsonObject().get("avatarUrls").getAsJsonObject().get("24x24").getAsString())));

			return users.stream().filter(x -> !x.getEmailAddress().contains("@connect.atlassian.com")).collect(Collectors.toList());
		} else return null;
	}

	public JiraProject[] getAllProjects() {
		Client client = Client.create();
		WebResource webResource = client.resource(url + "/rest/api/latest/project");

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		if (response.getStatus() == 200) {
			return new Gson().fromJson(response.getEntity(String.class), JiraProject[].class);
		} else return null;
	}

	public List<JiraIssueType> getAllIssuesTypes() {
		Client client = Client.create();
		WebResource webResource = client.resource(url + "/rest/api/latest/issuetype");

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		JiraIssueType[] issueList = new Gson().fromJson(response.getEntity(String.class), JiraIssueType[].class);

		return new ArrayList<>(Arrays.asList(issueList));
	}

	public List<JiraIssue> getAllIssuesForUser(String username) {
		Client client = Client.create();
		WebResource webResource = client.resource(url + StringFormatter.format("/rest/api/2/search?jql=assignee=%s", username).getValue());

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		JsonObject obj = parser.parse(response.getEntity(String.class)).getAsJsonObject();
		JsonArray issues = obj.get("issues").getAsJsonArray();
		if (issues.size() == 0) return null;
		JiraIssue[] issueList = new Gson().fromJson(issues, JiraIssue[].class);

		return new ArrayList<>(Arrays.asList(issueList));
	}

	public List<String> getAllIssueFieldsForUser(String username) {
		Client client = Client.create();
		WebResource webResource = client.resource(url + StringFormatter.format("/rest/api/2/search?jql=assignee=%s", username).getValue());

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		JsonObject obj = parser.parse(response.getEntity(String.class)).getAsJsonObject();
		JsonArray issues = obj.get("issues").getAsJsonArray();
		List<String> fields = new ArrayList<>();
		issues.forEach(x -> {
			fields.add(x.getAsJsonObject().get("fields").toString());
		});

		return fields;
	}

	public String getFirstIssueFieldsForUser(String username) {
		Client client = Client.create();
		WebResource webResource = client.resource(url + StringFormatter.format("/rest/api/2/search?jql=assignee=%s", username).getValue());

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		JsonObject obj = parser.parse(response.getEntity(String.class)).getAsJsonObject();
		JsonArray issues = obj.get("issues").getAsJsonArray();
		JsonElement fields = issues.get(0).getAsJsonObject().get("fields");

		return fields.toString();
	}

	public List<JiraIssueType> getIssueTypesForProject(String projectKey) {
		String address = url + "/rest/api/latest/issue/createmeta";
		Client client = Client.create();
		WebResource webResource = client.resource(address);

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		if (response.getStatus() == 200) {
			JsonObject obj = parser.parse(response.getEntity(String.class)).getAsJsonObject();
			JsonArray projects = obj.get("projects").getAsJsonArray();
			List<JiraIssueType> issues = new ArrayList<>();
			for (JsonElement object : projects) {
				if (!object.getAsJsonObject().get("key").getAsString().equals(projectKey)) continue;
				JsonArray types = object.getAsJsonObject().get("issuetypes").getAsJsonArray();
				for (JsonElement field : types) {
					issues.add(new JiraIssueType(field.getAsJsonObject().get("self").getAsString()
							, field.getAsJsonObject().get("id").getAsString()
							, field.getAsJsonObject().get("description").getAsString()
							, field.getAsJsonObject().get("iconUrl").getAsString()
							, field.getAsJsonObject().get("name").getAsString()
							, field.getAsJsonObject().get("subtask").getAsString()));
				}
			}
			return issues;
		}
		return null;
	}

	public List<JiraProject> getProjectsForIssueTypes(Long issueTypeId) {
		String address = url + "/rest/api/latest/issue/createmeta";
		Client client = Client.create();
		WebResource webResource = client.resource(address);

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		if (response.getStatus() == 200) {
			JsonObject obj = parser.parse(response.getEntity(String.class)).getAsJsonObject();
			JsonArray projects = obj.get("projects").getAsJsonArray();
			List<JiraProject> projList = new ArrayList<>();

			for (JsonElement object : projects) {
				boolean shouldAdd = false;
				for (JsonElement field : object.getAsJsonObject().get("issuetypes").getAsJsonArray()) {
					if (field.getAsJsonObject().get("id").getAsLong() == issueTypeId) {
						shouldAdd = true;
						break;
					}
				}
				if (shouldAdd) {
					projList.add(new JiraProject(null
							, object.getAsJsonObject().get("self").getAsString()
							, object.getAsJsonObject().get("id").getAsLong()
							, object.getAsJsonObject().get("key").getAsString()
							, object.getAsJsonObject().get("name").getAsString()
							, object.getAsJsonObject().get("avatarUrls").getAsJsonObject()
							, null));
				}
			}
			return projList;
		}
		return null;
	}

	public Map<String, JiraField> getCreateMetadata(String projectKey, Long issueId) {
		String address = String.format(
				url + "/rest/api/latest/issue/createmeta?projectKeys=%s&issuetypeIds=%d&expand=projects.issuetypes.fields"
				, projectKey, issueId);
		Client client = Client.create();
		WebResource webResource = client.resource(address);

		ClientResponse response = webResource.header("Authorization", "Basic " + auth)
				.type("application/json").accept("application/json").get(ClientResponse.class);

		if (response.getStatus() == 200) {

			JsonObject obj = parser.parse(response.getEntity(String.class)).getAsJsonObject();
			JsonArray proj = obj.get("projects").getAsJsonArray();
			if (proj.size() == 0) return null;
			JsonArray issueType = proj.get(0).getAsJsonObject().get("issuetypes").getAsJsonArray();
			if (issueType.size() == 0) return null;
			JsonObject fields = issueType.get(0).getAsJsonObject().get("fields").getAsJsonObject();

			Type type = new TypeToken<Map<String, TempObject>>() {
			}.getType();
			Map<String, TempObject> myMap = new Gson().fromJson(fields, type);
			return myMap.entrySet().stream().collect(Collectors.toMap(
					x -> x.getKey(), y ->
							new JiraField(y.getValue().name, y.getValue().schema.get("type").getAsString().equals("array")
									&& (y.getValue().schema.get("items").getAsString().equals("attachment") || y.getValue().schema.get("items").getAsString().equals("option")) ?
									y.getValue().schema.get("items").getAsString() : y.getValue().schema.get("type").getAsString()
									, null
									, y.getValue().allowedValues != null ?
									parseAllowedValues(y.getValue().allowedValues) : null)
			));
		} else return null;
	}

	private List<String> parseAllowedValues(JsonArray allowedValues) {
		List<String> list = new ArrayList<>();
		allowedValues.forEach(x -> {
			if (x.getAsJsonObject().get("value") != null)
				list.add(x.getAsJsonObject().get("value").getAsString());
		});
		return list;
	}

	public Response createIssue(String data, List<ObjectWrapper> attachments) {
		Client client = Client.create();
		WebResource webResource = client.resource(url + "/rest/api/latest/issue");
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);


		int status = response.getStatus();
		return Response.status(status)
				.entity((status == 200 || status == 201)
				? parser.parse(response.getEntity(String.class)).getAsJsonObject().get("id").getAsString()
				: response.getEntity(String.class))
				.build();
	}

	public Response addAttachments(String issueId, MultipartFile multipart) throws IOException {
		File fileToUpload = new File(multipart.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(fileToUpload);
		fos.write(multipart.getBytes());
		fos.close();

		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(MultiPartWriter.class);
		Client client = Client.create(cc);

		WebResource webResource = client.resource(
				StringFormatter.format("https://ziveit.atlassian.net/rest/api/latest/issue/%s/attachments", issueId).getValue());
		FileDataBodyPart filePart = new FileDataBodyPart("file",
				fileToUpload);
		MultiPart multipartEntity = new FormDataMultiPart()
				.bodyPart(filePart);

		ClientResponse response = webResource
				.header("X-Atlassian-Token", "nocheck")
				.header("Authorization", "Basic " + new String(Base64.encode("dominik.dujava@student.tuke.sk:126domino126")))
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.post(ClientResponse.class, multipartEntity);

		int status = response.getStatus();
		return Response.status(status).entity(response.getEntity(String.class)).build();
	}

	public String createIssueJson(Map<String, JiraField> map) {
		String data;
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("{\"fields\":{");
		map.forEach((key, value) -> {
			if (value.getValue() == null || value.getType().equals("array")) return;
			if (key.equals("project")) {
				stringBuilder.append("\"project\":{\"key\":\"").append(value.getValue()).append("\"},");
			}
			if (key.equals("issuetype")) {
				stringBuilder.append("\"issuetype\":{\"id\":\"").append(value.getValue()).append("\"},");
			}
			if (key.equals("priority")) {
				stringBuilder.append("\"priority\":{\"id\":\"").append(value.getValue()).append("\"},");
			}
			if (value.getType().equals("user")) {
				stringBuilder.append("\"").append(key).append("\":{\"name\":\"").append(value.getValue()).append("\"},");
			}
			if (value.getType().equals("option")) {
				stringBuilder.append("\"").append(key).append("\":{\"value\":\"").append(value.getValue()).append("\"},");
				return;
			}
			if (!key.equals("issuetype") && !key.equals("project") && !key.equals("priority") && !value.getType().equals("user"))
				if (value.getType().equals("number")) {
					stringBuilder.append("\"").append(key)
							.append("\":").append(value.getValue()).append(",");
				} else stringBuilder.append("\"").append(key)
						.append("\":\"").append(value.getValue()).append("\",");
		});
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		data = stringBuilder.append("}}").toString();
		return data;
	}

	class TempWrapper {
		String key;
		TempObject object;
	}

	class TempObject {
		Boolean required;
		JsonObject schema;
		String name;
		String autoCompleteUrl;
		Boolean hasDefaultValue;
		JsonArray operations;
		JsonArray allowedValues;
	}

}
