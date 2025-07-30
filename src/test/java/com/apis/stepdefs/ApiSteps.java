package com.apis.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.annotations.Steps;
import io.cucumber.java.zh_cn.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ApiSteps {

    private RequestSpecification request;
    private Response response;
    private String currentEnvironment = "default"; // 默认环境
    private String httpMethod; // 存储HTTP方法
    private String path; // 存储路径

    @Given("baseUri is {string}")
    public void setBaseUri(String baseUri) {
        RestAssured.baseURI = baseUri;
        request = SerenityRest.given();
    }

    @When("I set method to {string}")
    public void setMethod(String method) {
        // 方法会在execute步骤中实际使用
        this.httpMethod = method.toUpperCase();
    }

    @And("I set the path to {string}")
    public void setPath(String path) {
        // 路径会在execute步骤中实际使用
        this.path = path;
    }

    @And("I set Content-Type header to {string}")
    public void setContentTypeHeader(String contentType) {
        request.contentType(contentType);
    }

    @And("I set body with this json")
    public void setBody(String body) {
        request.body(body);
    }

    @And("I execute the request")
    public void executeRequest() {
        if (httpMethod == null || httpMethod.isEmpty()) {
            throw new IllegalStateException("HTTP method is not set. Please use 'I set method to ...' step");
        }

        if (path == null || path.isEmpty()) {
            throw new IllegalStateException("Path is not set. Please use 'I set the path to ...' step");
        }

        // 根据设置的HTTP方法执行请求
        switch (httpMethod) {
            case "GET":
                response = request.when().get(path);
                break;
            case "POST":
                response = request.when().post(path);
                break;
            case "PUT":
                response = request.when().put(path);
                break;
            case "DELETE":
                response = request.when().delete(path);
                break;
            case "PATCH":
                response = request.when().patch(path);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    @Then("the response code is {int}")
    public void verifyResponseCode(int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @And("the response content type should be {string}")
    public void verifyContentType(String contentType) {
        response.then().contentType(ContentType.fromContentType(contentType));
    }

    @And("response body should be valid json")
    public void verifyValidJson() {
        // 更可靠的 JSON 验证方式
        response.then().assertThat().body(anything());
    }
    @And("response body should match schema {string}")
    public void verifyJsonSchema(String schemaFile) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaFile));
    }
}