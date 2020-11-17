Feature: Query incomplete tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.

  Background:
    Given system is ready
    And the following projects are created in the system:
      | title   | completed   | active	 | description |
      | course1 | false       | true     | desc1       |
      | course2 | false       | true     | desc2       |
      | course3 | false       | true     | desc3       |
      | course4 | false       | true     | desc4       |
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | true        | test3       |
      | test4 | false       | test4       |
      | test5 | true        | test5       |
    And the following todos are tasks of 'course1'
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | true        | test3       |
    And the following todos are tasks of 'course2'
      | title | doneStatus  | description |
      | test4 | false       | test4       |
    And the following todos are tasks of 'course3'
      | title | doneStatus  | description |
      | test5 | true        | test45      |
    And 'course4' does not have any todos

  Scenario Outline: student queries incomplete task of a project. (Normal flow)
    Given the todo <title> exists in the system
    And the project <project> exists in the system
    When query incomplete todos of project <title>
    Then each todo of project <title> returned will be marked as done

    Examples:
      | project   | title     |
      | course1   | test1     |
      | course2   | test2     |

  Scenario Outline: student queries incompleted tasks of project with no incomplete tasks. (Alt flow)
    Given the todo <title> exists in the system
    And the project <project> exists in the system
    When query incomplete todos of project <project>
    Then no todos should be returned for project <project>
    Examples:
      | project   | title |
      | course3   | test5 |

  Scenario Outline: student queries incompleted tasks of project with no tasks. (Alt flow)
    Given the project <project> exists in the system
    And the project <project> has no todos
    When query incomplete todos of project <project>
    Then no todos should be returned for project <project>
    Examples:
      | project   |
      | course4   |

  Scenario Outline: student query incompleted tasks of a non-existing project. (Error flow)
    Given the project <project> does not exist in the system
    When query incomplete todos of non-existing project <project>
    Then an error not found message should be displayed
    Examples:
      | project   |
      | course6   |
