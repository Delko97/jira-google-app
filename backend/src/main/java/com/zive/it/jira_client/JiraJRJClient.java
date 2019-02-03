package com.zive.it.jira_client;

import com.atlassian.httpclient.apache.httpcomponents.Headers;
import com.atlassian.jira.rest.client.api.GetCreateIssueMetadataOptions;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.api.domain.CimProject;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atlassian.jira.rest.client.api.GetCreateIssueMetadataOptions.EXPAND_PROJECTS_ISSUETYPES_FIELDS;

public class JiraJRJClient {

	private String username;
	private String password;
	private String jiraUrl;
	public JiraRestClient restClient;

	List<String> listExpandos = new ArrayList<String>();


	public JiraJRJClient(String username, String password, String jiraUrl) {
		this.username = username;
		this.password = password;
		this.jiraUrl = jiraUrl;
		this.restClient = getJiraRestClient();

		listExpandos.add(EXPAND_PROJECTS_ISSUETYPES_FIELDS);
	}

	public JiraRestClient getJiraRestClient() {
//		System.out.println(String.format("Logging in to %s with username '%s' and password '%s'",
//				getJiraUri(), this.username, this.password));
		return new AsynchronousJiraRestClientFactory()
				.createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
	}

	public URI getJiraUri() {
		return URI.create(this.jiraUrl);
	}

	public void createIssue(Long issueType, String projectKey){
		final IssueInputBuilder issueBuilder = new IssueInputBuilder(projectKey, issueType, "summary");
		issueBuilder.setDueDate(new DateTime());

		restClient.getIssueClient().createIssue(issueBuilder.build());
	}

	public Map<String, CimFieldInfo> getFieldsOfIssue(List<Long> issueIds, List<Long> projectIds){
		Promise<Iterable<CimProject>> metadata = restClient.getIssueClient().getCreateIssueMetadata(
				new GetCreateIssueMetadataOptions(this.listExpandos, null, issueIds,null, projectIds));
		Iterable<CimProject> projectMetadata = metadata.claim();
		if (projectMetadata.iterator().hasNext()){
			return projectMetadata.iterator().next().getIssueTypes().iterator().next().getFields();
		}else {
			return null;
		}
	}





}
