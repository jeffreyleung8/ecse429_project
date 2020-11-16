Feature: Query incomplete tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.

  Background:
    Given system is ready
    And the following project is created in the system:
      | title   | completed   | active	 | description |
      | course1 | false       | true     | desc1       |
      | course2 | false       | true     | desc2       |
    And the following todos are created in the system:
      | title | doneStatus  | description |
      | test1 | false       | test1       |
      | test2 | false       | test2       |
      | test3 | true        | test3       |
      | test4 | false       | test4       |
    And the following relationships are created in the system:
      | todo  | taskof  |
      | test1 | course1 |
      | test2 | course1 |
      | test3 | course1 |
      | test4 | course2 |

  Scenario Outline: student queries incomplete tasks of a project. (Normal flow)
    Given the todo <title> exists in the system
    And the project <title> exists in the system
    And the todos are under the project <title>
    When query project <title>
    And query todos with <doneStatus> as false
    Then all todos with <doneStatus> as false of project <title> should be displayed.
    Examples:
    | title | doneStatus | description |
    | test1 | false      | test1       |
    | test2 | false      | test2       |

  Scenario Outline: student queries incompleted tasks of all projects. (Alt flow)
    Given the todo <title> exists in the system
    And the project <title> exists in the system
    When query todos with <doneStatus> as false
    Then all todos with <doneSatus> as false should be displayed.
    Examples:
      | title |
      | test1 |
      | test2 |
      | test4 |

  Scenario Outline: student queries incompleted tasks of non existing project. (Error flow)
    Given the todo <title> exists in the system
    And the project <title> does not exist in the system
    When query the project <title> from the system
    Then an error not found message should be displayed.
    Examples:
      | title |
      | test1 |
      | test2 |
      | test3 |

  Scenario Outline: student query non existing incompleted tasks of a project(Error flow)
    Given the todo <title> does not exist in the system
    And the project <title does exist in the system
    When query todos with <doneStatus> as false
    Then an error not found message should be displayed.
    Examples:
      | title |
      | test5 |
      | test6 |
