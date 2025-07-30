@logs
Feature: API function - Search logs and update log with audit actions
  In order to test

  Background:
    Given baseUri is https://api.simcotools.com
  @NO_UI @be @log_list
  Scenario Outline: Get the last VWAP calculated for an specific resource and quality
    When I set Content-Type header to application/json
    And I set method to GET
    And I set the path to "<path>"
    And I execute the request
    Then the response code is <responseCode>
    And the response content type should be "application/json"
    And response body should be valid json
    Examples:
      | path                                 | responseCode |
      | /v1/realms/{realm}/market/vwaps/83/4 | 200          |
