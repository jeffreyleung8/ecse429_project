Feature: Change task description
  As a student, I want to change a task description, to better represent the work to do.

  Background:
    Given system is ready
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | false       | test3       |

  Scenario Outline: student changes the description of a task once (Normal flow)
    Given the todo <title> exists in the system
    And <oldDescription> is the description of todo <title>
    When change <oldDescription> of todo <title> to <newDescription>
    Then the description of todo <title> should be <newDescription>
    Examples:
    | title | oldDescription | newDescription |
    | test1 | test1          | hello          |
    | test2 | test2          | world          |
    | test3 | test3          | test4          |

Scenario Outline: student removes description. (Alt flow)
    Given the todo <title> exists in the system
    And <oldDescription> is the description of todo <title>
    When change <oldDescription> of todo <title> to <newDescription>
    Then the description of todo <title> should be <newDescription>
    Examples:
      | title | oldDescription | newDescription |
      | test1 | test1          | ""             |
      | test2 | test2          | ""             |
      | test3 | test3          | ""             |

 Scenario Outline: student changes the description of a non existing todo (Error flow)
    Given the todo <title> does not exist in the system
    When change <oldDescription> of todo <title> to <newDescription>
    Then an error not found message should be displayed
    Examples:
      | title |
      | test4 |
      | test5 |
