Feature: Categorize task priority
  As a student, I categorize tasks as HIGH, MEDIUM or LOW priority, so I can better manage my time.

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

  Scenario Outline: student creates a todo with priority (Normal Flow)
    Given the todo <title> exists in the system
    And the category <priority> exists in the system
    When categorize todo <title> as category <priority>
    Then the category of the todo <title> should be <priority>
    Examples:
      | title | priority |
      | test1 | LOW      |
      | test2 | MEDIUM   |
      | test3 | HIGH     |

  Scenario Outline: student creates multiple todos with the same priority (Alt flow)
    Given the todo <title1> exists in the system
    And the todo <title2> exists in the system
    And the category <priority> exists in the system
    When categorize todo <title1> as category <priority>
    And categorize todo <title2> as category <priority>
    Then the category of the todo <title1> should be <priority>
    And the category of the todo <title2> should be <priority>
    Examples:
      | title1 | title2 | priority |
      | test1  | test2  | LOW      |
      | test2  | test3  | MEDIUM   |
      | test3  | test1  | HIGH     |

  Scenario Outline: student creates multiple todos with different priority (Alt flow)
    Given the todo <title1> exists in the system
    And the todo <title2> exists in the system
    And the category <priority1> exists in the system
    And the category <priority2> exists in the system
    When categorize todo <title1> as category <priority1>
    And categorize todo <title2> as category <priority2>
    Then the category of the todo <title1> should be <priority1>
    And the category of the todo <title2> should be <priority2>
    Examples:
      | title1 | title2 | priority1 | priority2 |
      | test1  | test2  | LOW       | MEDIUM    |
      | test2  | test3  | MEDIUM    | HIGH      |
      | test3  | test1  | HIGH      | LOW       |

  Scenario Outline: student changes the priority of a task to an non existing priority (Error flow)
    Given the todo <title> exists in the system
    And the category <priority> does not exist in the system
    When categorize todo <title> as category <priority>
    Then an error not found message should be displayed
    Examples:
      | title | priority |
      | test1 | NONE     |
      | test2 | NO       |
      | test3 | YES      |