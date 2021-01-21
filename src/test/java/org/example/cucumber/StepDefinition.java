package org.example.cucumber;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class StepDefinition {

    private static final String CREATE_PATH = "/create";
    private static final String APPLICATION_JSON = "application/json";

    // POST REQUEST PAYLOAD
    private final InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("cucumber.json");
    private final String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @When("^users upload data on a project$")
    public void usersUploadDataOnAProject() throws IOException {

        HttpPost request = new HttpPost("https://jsonplaceholder.typicode.com" + "" + "/posts");
        StringEntity entity = new StringEntity(jsonString);
        request.addHeader("content-type", APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString = convertResponseToString(httpResponse);

        System.out.println(responseString);

        assertEquals(201, httpResponse.getStatusLine().getStatusCode());
        assertThat(responseString, containsString("\"id\": 101"));
        assertThat(responseString, containsString("\"userId\": 1"));
        assertThat(responseString, containsString("\"body\": \"bar\""));
        assertThat(responseString, containsString("\"title\": \"foo\""));
    }

//    @When("^users want to get information on the '(.+)' project$")
    @When("users want to get information on the {string} project")
    public void usersGetInformationOnAProject(String projectName) throws IOException {

        HttpGet request = new HttpGet("https://jsonplaceholder.typicode.com" + "" + "/posts/1");
        request.addHeader("accept", APPLICATION_JSON);
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString = convertResponseToString(httpResponse);

        System.out.println(responseString);

        assertThat(responseString, containsString("\"id\": 1"));
        assertThat(responseString, containsString("\"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\""));
    }

    @Then("the server should handle it and return a success status")
    public void theServerShouldReturnASuccessStatus() {
        System.out.println("The server returning a success status!");
    }

    @Then("the requested data is returned")
    public void theRequestedDataIsReturned() {
        System.out.println("The requested data is returned!");
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }
}