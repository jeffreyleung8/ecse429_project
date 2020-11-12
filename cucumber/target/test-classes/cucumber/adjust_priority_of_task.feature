Feature: Adjust task priority
  As a student, I want to adjust the priority of a task, to help better manage my time.

  Background:
    Given system is ready
    And the following categories are created in the system:
      | title   | description       |
      | HIGH    | "high priority"   |
      | MEDIUM  | "medium priority" |
      | LOW     | "low priority"    |
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | false       | test3       |

  Scenario Outline: student changes the priority of a task once (Normal flow)
    Given the todo <title> exists in the system
    And the category <oldPriority> exists in the system
    And the category <newPriority> exists in the system
    When categorize todo <title> as category <oldPriority>
    And remove category <oldPriority> from todo <title>
    And categorize todo <title> as category <newPriority>
    Then the category of the todo <title> should be <newPriority>
    Examples:
    | title | oldPriority | newPriority |
    | test1 | LOW         | HIGH        |
    | test2 | HIGH        | LOW         |
    | test3 | MEDIUM      | LOW         |

  Scenario Outline: student changes the priority of a task from LOW to MEDIUM and to HIGH (Alt flow)
    Given the todo <title> exists in the system
    When categorize todo <title> as category LOW
    And remove category LOW from todo <title>
    And categorize todo <title> as category MEDIUM
    And remove category MEDIUM from todo <title>
    And categorize todo <title> as category HIGH
    Then the category of the todo <title> should be HIGH
    Examples:
      | title |
      | test1 |
      | test2 |
      | test3 |

  Scenario Outline: student changes the priority of a non existing todo (Error flow)
    Given the todo <title> exists in the system
    When remove the todo <title> from the system
    And categorize todo <title> as category HIGH
    Then the return code should be 404
    Examples:
      | title |
      | test1 |
      | test2 |
      | test3 |

  Scenario Outline: student changes the priority of a removed todo (Error flow)
    Given the todo <title> does not exist in the system
    And categorize todo <title> as category HIGH
    Then the return code should be 404
    Examples:
      | title |
      | test5 |
      | test6 |