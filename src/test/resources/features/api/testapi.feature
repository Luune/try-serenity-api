@SIT
Feature: API function - Search logs and update log with audit actions
  In order to test simcotools

  Background:
    Given baseUri is "https://api.simcotools.com"
  @NO_UI @VWAP
  Scenario Outline: Get the last VWAP calculated for an specific resource and quality
    When I set method to "GET"
    And I set the path to "<path>"
    And I set Content-Type header to "application/json"
    And I execute the request
    Then the response code is <responseCode>
    And the response content type should be "application/json"
    And response body should be valid json
    Examples:
      | path                                 | responseCode |
      | /v1/realms/0/market/vwaps/83/4       | 200          |

  @NO_UI @price1
  Scenario Outline: Get the price of 柴油Q4
    When I set method to "GET"
    And I set the path to "<path>"
    And I set params as list
      | granularity | 1d                    |
      | start       | "2025-07-30 00:00:00" |
      | end         | "2025-07-31 00:00:00" |
    And I execute the request
    Then the response code is <responseCode>
    And the response content type should be "application/json"
    And response body should be valid json
    Examples:
      | path                                 | responseCode |
      | /v1/realms/0/market/resources/12/4/candlesticks       | 200          |
