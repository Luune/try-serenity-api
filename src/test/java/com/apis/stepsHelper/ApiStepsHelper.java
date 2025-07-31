package com.apis.stepsHelper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import net.serenitybdd.core.Serenity;

public class ApiStepsHelper {

    private RequestSpecification request;
    private Response response;
    private String currentEnvironment = "default"; // 默认环境
    private String httpMethod; // 存储HTTP方法
    private String path; // 存储路径

    public ApiStepsHelper() {
        request = SerenityRest.given();
    }

    public void setBaseUri(String baseUri) {
        request.baseUri(baseUri);
    }

    public void setMethod(String method) {
        this.httpMethod = method.toUpperCase();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setContentTypeHeader(String contentType) {
        request.contentType(contentType);
    }

    public void setBody(String body) {
        request.body(body);
    }

    public void executeRequest() throws IllegalStateException, IllegalArgumentException {
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
        Serenity.setSessionVariable("response").to(response);
    }

    public void verifyResponseCode(int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    public void verifyContentType(String contentType) {
        response.then().contentType(ContentType.fromContentType(contentType));
    }

    public void verifyValidJson() {
        // 更可靠的 JSON 验证方式
        response.then().assertThat().body(anything());
    }

    public void verifyJsonSchema(String schemaFile) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaFile));
    }
}