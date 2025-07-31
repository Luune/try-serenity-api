@pet
Feature: Get Kitty

  @checkpet
  Scenario: Get Kitty ID
    Given Kitty is available in the pet store
    When I ask for a pet using Kitty's ID
    Then I get Kitty as result