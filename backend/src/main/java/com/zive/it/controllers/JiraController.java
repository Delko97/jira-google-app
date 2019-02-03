package com.zive.it.controllers;

import com.zive.it.jira_client.JiraClient;
import com.zive.it.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/rest")
@CrossOrigin
public class JiraController {

	@Autowired
	JiraClient client;

	@GetMapping(path = "/projects")
	public JiraProject[] getAllProjects(){
		return client.getAllProjects();
	}

	@GetMapping(path = "/projects/{issueTypeId}")
	public List<JiraProject> getProjectsForIssue(@PathVariable Long issueTypeId){
		return client.getProjectsForIssueTypes(issueTypeId);
	}

	@GetMapping(path = "/issuetypes/{projectKey}")
	public List<JiraIssueType> getIssuesTypesForProject(@PathVariable String projectKey){
		return client.getIssueTypesForProject(projectKey);
	}

	@GetMapping(path = "/issuetypes")
	public List<JiraIssueType> getAllIssuesTypes(){
		return client.getAllIssuesTypes();
	}

	@GetMapping(path = "/users")
	public List<JiraUser> getAllUsers() {
		return client.getAllUsers();
	}

	@GetMapping(path = "/user/{assignee}/issues")
	public List<String> getAllIssuesForUser(@PathVariable String assignee) {
		return client.getAllIssueFieldsForUser(assignee);
	}

	@GetMapping(path = "/user/{assignee}/firstissue")
	public String getFirstIssuesForUser(@PathVariable String assignee) {
		return client.getFirstIssueFieldsForUser(assignee);
	}

	@GetMapping(path = "/metadata/{issueTypeId}/{projectKey}")
	public Map<String, JiraField> getMetadata(@PathVariable Long issueTypeId, @PathVariable String projectKey){

		return client.getCreateMetadata(projectKey, issueTypeId);
	}

	@GetMapping(value = "/metadatalist/{issueTypeId}/{projectKey}", produces = "application/json")
	public List<ObjectWrapper> getMetadataAsList(@PathVariable Long issueTypeId, @PathVariable String projectKey){
		List<ObjectWrapper> list = new ArrayList<>();
		client.getCreateMetadata(projectKey, issueTypeId).forEach((key, value) -> list.add(new ObjectWrapper(key, value)));
		list.forEach(x -> {
			if (x.getKey().equals("project")) x.getField().setValue(projectKey);
			if (x.getKey().equals("issuetype")) x.getField().setValue(issueTypeId.toString());
		});
		return list;
	}

	@PostMapping("/create")
	public Response createIssue(@RequestBody List<ObjectWrapper> object) {
		String createIssueString = client.createIssueJson(object.stream().collect(
				Collectors.toMap(ObjectWrapper::getKey, ObjectWrapper::getField)));
		return client.createIssue(createIssueString, object.stream().filter(x -> x.getField().getType().equals("array")).collect(Collectors.toList()));
	}

	@PostMapping("/file/{issueId}")
	public Response file(@PathVariable String issueId, MultipartHttpServletRequest request) throws IOException {
		Iterator<String> itr = request.getFileNames();
		return client.addAttachments(issueId, request.getFile(itr.next()));
	}
}
