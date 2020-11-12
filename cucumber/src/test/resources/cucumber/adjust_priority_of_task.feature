Feature: Adjust task priority
  As a student, I want to adjust the priority of a task, to help better manage my time.

  Background:
    Given system is ready
    And the following categories are created in the system:
      | title   | description     |
      | HIGH    | "high priority"   |
      | MEDIUM  | "medium priority" |
      | LOW     | "low priority"    |

  Scenario: student changes the priority of a task from LOW to HIGH (Normal flow)
    Given the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
    And categorize todo "test1" as category "LOW"
    When remove category "LOW" from todo "test1"
    And categorize todo "test1" as category "HIGH"
    Then the category of the todo "test1" should be "HIGH"

  Scenario: student changes the priority of a task from LOW to MEDIUM and to HIGH (Alt flow)
    Given the following todos are created in the system:
      | title | doneStatus  | description |
      | test2 | false       | test2       |
    And categorize todo "test2" as category "LOW"
    When remove category "LOW" from todo "test2"
    And categorize todo "test2" as category "MEDIUM"
    And remove category "MEDIUM" from todo "test2"
    And categorize todo "test2" as category "HIGH"
    Then the category of the todo "test2" should be "HIGH"

  Scenario: student changes the priority of a non existing todo (Error flow)
    Given the following todos are created in the system:
      | title | doneStatus  | description |
      | test3 | false       | test3       |
    When remove the todo "test3"
    And categorize todo "test3" as category "HIGH"
    Then the return code should be "404"