Feature: Query all the tasks under a priority
  As a student, I query all incomplete HIGH priority tasks from all my classes, to identity my short-term goals.

  Background:
    Given system is ready
    And the following categories are created in the system:
      | title   | description       |
      | HIGH    | "high priority"   |
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | true        | test3       |
      | test4 | true        | test4       |

  Scenario Outline: student adds priority HIGH to a incomplete task and retrieves it (Normal Flow)
    Given the todo <title> exists in the system
    And the todo <title> is marked incomplete
    And the category <priority> exists in the system
    When categorize todo <title> as category <priority>
    Then the todo <title> with status incomplete should be under the category <priority>
    Examples:
      | title | priority   |
      | test1 | HIGH       |
      | test2 | HIGH       |

  Scenario Outline: student adds incomplete task to priority HIGH and retrieves it (Normal flow)
    Given the todo <title> exists in the system
    And the todo <title> is marked incomplete
    And the category <priority> exists in the system
    When add the todo <title> to the category <priority>
    Then the todo <title> with status incomplete should be under the category <priority>
    Examples:
      | title | priority   |
      | test1 | HIGH       |
      | test2 | HIGH       |

  Scenario Outline: student adds completed tasks to HIGH priority (Alt flow)
    Given the todo <title> exists in the system
    And the todo <title> is marked completed
    When add the todo <title> to the category <priority>
    Then the list of todos with status incomplete under the category <priority> should be empty
    Examples:
      | title  |  priority |
      | test3  |  HIGH     |
      | test4  |  HIGH     |

  Scenario Outline: student adds tasks to HIGH priority and marks it completed (Alt flow)
    Given the todo <title> exists in the system
    When add the todo <title> to the category <priority>
    And marking the todo <title> as completed
    Then the list of todos with status incomplete under the category <priority> should be empty
    Examples:
      | title  |  priority |
      | test1  |  HIGH     |
      | test2  |  HIGH     |

  Scenario Outline: student adds tasks to HIGH priority and removes it (Alt flow)
    Given the todo <title> exists in the system
    When add the todo <title> to the category HIGH
    And remove the todo <title> from the system
    Then the list of todos with status incomplete under the category HIGH should be empty
    Examples:
      | title  |
      | test1  |
      | test2  |

  Scenario Outline: student adds a non exiting task to HIGH and retrieves it (Error flow)
    Given the todo <title> does not exist in the system
    When categorize todo <title> as category HIGH
    Then an error not found message should be displayed
    Examples:
      | title   |
      | test10  |
      | test20  |