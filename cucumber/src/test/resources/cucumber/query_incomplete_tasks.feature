Feature: Query incomplete tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.

  Background:
    Given system is ready

  Scenario Outline: student queries incomplete task of a project. (Normal flow)
    Given the following projects are created in the system:
      | title     | completed   | active	 | description   |
      | <project> | false       | true       | <description> |
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | true        | test2       |
      | test3 | true        | test3       |
    And the following todos are tasks of project <project>
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | true        | test2       |
      | test3 | true        | test3       |
    When query incomplete todos of project <project>
    Then <n> incomplete todo of project <project> will be returned

    Examples:
      | project   | description  | n  |
      | course1   | desc1        | 1  |
      | course2   | desc2        | 1  |

  Scenario Outline: student queries incompleted tasks of project with no incomplete tasks. (Alt flow)
    Given the following projects are created in the system:
      | title     | completed   | active	 | description   |
      | <project> | false       | true       | <description> |
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test4 | true        | test1       |
      | test5 | true        | test2       |
      | test6 | true        | test3       |
    And the following todos are tasks of project <project>
      | title | doneStatus  | description |
      | test4 | true        | test1       |
      | test5 | true        | test2       |
      | test6 | true        | test3       |
    And the project with title <project> has no incomplete tasks
    When query incomplete todos of project <project>
    Then no todos should be returned for project <project>
    Examples:
      | project   | description  |
      | course3   | desc3        |
      | course4   | desc4        |

  Scenario Outline: student queries incompleted tasks of project with no tasks. (Alt flow)
    Given the following projects are created in the system:
      | title     | completed   | active	 | description   |
      | <project> | false       | true       | <description> |
    And the project with title <project> has no incomplete tasks
    When query incomplete todos of project <project>
    Then no todos should be returned for project <project>
    Examples:
      | project   | description  |
      | course5   | desc5        |
      | course6   | desc6        |

  Scenario Outline: student query incompleted tasks of a non-existing project. (Error flow)
    Given the project <project> does not exist in the system
    When query incomplete todos of non-existing project <project>
    Then an error not found message should be displayed
    Examples:
      | project    |
      | course98   |
      | course99   |
