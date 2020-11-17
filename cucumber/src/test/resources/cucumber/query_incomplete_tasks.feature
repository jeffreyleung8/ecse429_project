Feature: Query incomplete tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.

  Background:
    Given system is ready
    And the following project is created in the system:
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
    And the following todos are tasks of 'course1':
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | true        | test3       |
    And the following todos are tasks of 'course2':
      | title | doneStatus  | description |
      | test4 | false       | test4       |
    And the following todos are tasks of 'course3':
      | title | doneStatus  | description |
      | test5 | true        | test45      |

  Scenario Outline: student queries incomplete task of a project. (Normal flow)
    Given the todo <title> exists in the system
    And the project <title> exists in the system
    When query incomplete todos of project <title>
    Then each todo returned will have <doneSatus> false
    And each todo will be a task of project <title>.

    Examples:
    | title | doneStatus | description |
    | test1 | false      | test1       |
    | test2 | false      | test2       |

  Scenario Outline: student queries incompleted tasks of project with no incomplete tasks. (Alt flow)
    Given the todo <title> exists in the system
    And the project <title> exists in the system
    When query incomplete todos of project <title>
    Then no todos should be returned for project <title>.
    Examples:
      | title   |
      | course3 |

  Scenario Outline: student queries incompleted tasks of project with no tasks. (Alt flow)
    Given the project <title> exists in the system
    And the project <title> has no tasks
    When query incomplete todos of project <title>
    Then no todos should be returned for project <title>.
    Examples:
      | title   |
      | course4 |

  Scenario Outline: student query incompleted tasks of a non-existing project. (Error flow)
    Given the todo <title> exists in the system
    And the project <title> does not exist in the system
    When query incomplete todos of non-existing project <title>
    Then an error not found message should be displayed.
    Examples:
      | title   |
      | course4 |
      | course5 |
