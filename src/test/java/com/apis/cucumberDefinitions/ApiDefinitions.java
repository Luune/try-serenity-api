package com.apis.cucumberDefinitions;

import com.apis.stepsHelper.ApiStepsHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

public class ApiDefinitions {

    @Steps
    private ApiStepsHelper apiStepsHelper;

    @Given("baseUri is {string}")
    public void setBaseUri(String baseUri) {
        apiStepsHelper.setBaseUri(baseUri);
    }

    @When("I set method to {string}")
    public void setMethod(String method) {
        apiStepsHelper.setMethod(method);
    }

    @And("I set the path to {string}")
    public void setPath(String path) {
        apiStepsHelper.setPath(path);
    }

    @And("I set Content-Type header to {string}")
    public void setContentTypeHeader(String contentType) {
        apiStepsHelper.setContentTypeHeader(contentType);
    }

    @And("I set body with this json")
    public void setBody(String body) {
        apiStepsHelper.setBody(body);
    }

    @And("I execute the request")
    public void executeRequest() throws IllegalStateException, IllegalArgumentException {
        apiStepsHelper.executeRequest();
    }

    @Then("the response code is {int}")
    public void verifyResponseCode(int expectedCode) {
        apiStepsHelper.verifyResponseCode(expectedCode);
    }

    @And("the response content type should be {string}")
    public void verifyContentType(String contentType) {
        apiStepsHelper.verifyContentType(contentType);
    }

    @And("response body should be valid json")
    public void verifyValidJson() {
        apiStepsHelper.verifyValidJson();
    }

    @And("response body should match schema {string}")
    public void verifyJsonSchema(String schemaFile) {
        apiStepsHelper.verifyJsonSchema(schemaFile);
    }
}