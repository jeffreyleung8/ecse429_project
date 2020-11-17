Feature: Change task description
  As a student, I want to change a task description, to better represent the work to do.

  Background:
    Given system is ready

  Scenario Outline: student changes the description of a task once (Normal flow)
    Given the following todos are created in the system:
      | title   | doneStatus  | description       |
      | <title> | false       | <oldDescription>  |
    And <oldDescription> is the description of todo <title>
    When change <oldDescription> of todo <title> to <newDescription>
    Then the description of todo <title> should be <newDescription>
    Examples:
    | title | oldDescription | newDescription |
    | task1 | test1          | hello          |
    | task2 | test2          | world          |
    | task3 | test3          | test4          |

Scenario Outline: student removes description. (Alt flow)
    Given the following todos are created in the system:
      | title   | doneStatus  | description       |
      | <title> | false       | <oldDescription>  |
    And <oldDescription> is the description of todo <title>
    When removing the description of the todo <title>
    Then the description of todo <title> should now be removed
    Examples:
      | title | oldDescription | newDescription |
      | task4 | test1          | ""             |
      | task5 | test2          | ""             |
      | task6 | test3          | ""             |

 Scenario Outline: student changes the description of a non existing todo (Error flow)
    Given the todo <title> does not exist in the system
    When change <oldDescription> of todo <title> to <newDescription>
    Then an error not found message should be displayed
    Examples:
      | title | oldDescription | newDescription |
      | task7 | test1          | hey            |
      | task8 | test2          | bye            |
