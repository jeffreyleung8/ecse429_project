Feature: Categorize task priority
  As a student, I categorize tasks as HIGH, MEDIUM or LOW priority, so I can better manage my time.

  Background:
    Given system is ready
    And the following categories are created in the system:
      | title   | description     |
      | HIGH    | "high priority"   |
      | MEDIUM  | "medium priority" |
      | LOW     | "low priority"    |

  Scenario: student creates a todo with HIGH priority (Normal Flow)
    Given the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
    When categorize todo "test1" as category "HIGH"
    Then the category of the todo "test1" should be "HIGH"

  Scenario: student creates multiple todos with LOW priority (Alt flow)
    Given the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
    When categorize todo "test1" as category "LOW"
    And categorize todo "test2" as category "LOW"
    Then the category of the todo "test1" should be "LOW"
    And the category of the todo "test2" should be "LOW"

  Scenario: student changes the priority of a task to an non existing priority (Error flow)
    Given the following todos are created in the system:
      | title | doneStatus  | description |
      | test3 | false       | test3       |
    When categorize todo "test2" as category "NONE"
    Then the return code should be "404"