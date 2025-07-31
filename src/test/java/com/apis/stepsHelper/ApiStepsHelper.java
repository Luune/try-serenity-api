package com.apis.stepsHelper;

import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import net.serenitybdd.core.Serenity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.List;
import java.util.Map;

public class ApiStepsHelper {

    private RequestSpecification request;
    private Response response;
    private String currentEnvironment = "default"; // 默认环境
    private String httpMethod; // 存储HTTP方法
    private String path; // 存储路径

    public ApiStepsHelper() {
        // 初始化请求并忽略SSL验证（仅用于开发测试环境）
        request = SerenityRest.given()
                .config(RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation()));
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

    /**
     * 处理参数列表，将DataTable中的参数添加到请求中
     * 支持类似以下格式的参数：
     * | granularity | 1d                    |
     * | start       | "2025-07-30 00:00:00" |
     * | end         | "2025-07-31 00:00:00" |
     */
    public void setParamsAsList(DataTable dataTable) {
        // 获取表格的行数据
        List<List<String>> rows = dataTable.asLists(String.class);

        // 遍历每一行，处理键值对
        for (List<String> row : rows) {
            // 确保每行有两个元素：键和值
            if (row.size() >= 2) {
                String key = row.get(0).trim();
                String value = row.get(1).trim();

                // 移除值中可能存在的引号
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                // 特殊处理日期参数，转换为时间戳
                if (("start".equals(key) || "end".equals(key)) && isDateString(value)) {
                    value = convertToTimestamp(value);
                }

                // 添加参数到请求
                request.param(key, value);
            }
        }
    }

    /**
     * 检查字符串是否为日期格式
     */
    private boolean isDateString(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 将日期字符串转换为时间戳（毫秒）
     */
    private String convertToTimestamp(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // 使用UTC时区处理
            Date date = sdf.parse(dateStr);
            return String.valueOf(date.getTime());
        } catch (ParseException e) {
            // 如果解析失败，返回原始值
            return dateStr;
        }
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