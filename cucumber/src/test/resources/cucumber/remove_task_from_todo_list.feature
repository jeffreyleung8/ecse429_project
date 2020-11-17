Feature: Remove task from course todo list
  As a student, I remove an unnecessary task from my course to do list, so I can forget about it.

  Background: 
    Given system is ready
    And the following projects are created in the system:
      | title   | completed   | active	 | description |
      | course1 | false       | true     | desc1       |
      | course2 | false       | false    | desc2       |
      | course3 | true        | false    | desc3       |
      | course4 | true        | false    | desc4       |
    And the following todos are created in the system:   
      | title   | doneStatus  | description |
      | todo1   | false       | test1       |
      | todo2   | false       | test2       |
      | todo3   | false       | test3       |
      | todo4   | false       | test3       |
    And the following todos are tasks of ≈
      | title   | doneStatus  | description |
      | todo1   | false       | test1       |
      | todo2   | false       | test2       |
      | todo3   | false       | test1       |
      | todo4   | false       | test2       |
    And the following todos are tasks of 'course2'
      | title   | doneStatus  | description |
      | todo1   | false       | test1       |
      | todo2   | false       | test2       |
      | todo3   | false       | test1       |
      | todo4   | false       | test2       |
    And the following todos are tasks of 'course3'
      | todo3   | false       | test3       |
      | todo4   | false       | test3       |
    And 'course4' does not have any todos
	
  Scenario Outline: Remove a valid todo from a course todo list (Normal Flow)
    Given the todo <title> exists in the system
    And the project <project> exists in the system
    And the todo <title> is a task of the project <project>
    When removing the todo <title> from the project <project> to do list
    Then the todo <title> should not be part of the project <project> to do list in the system 

    Examples: 
      | project   | title     | 
      | course1   | todo1     | 
      | course2   | todo2     |     
      
  Scenario Outline: Remove multiple todos from the same course todo list (Alt Flow)
    Given the todo <title1> exists in the system
    And the todo <title2> exists in the system
    And the project <project> exists in the system
    When removing the todo <title1> from the project <project> to do list
    And removing the todo <title2> from the project <project> to do list
    Then the todo <title1> should not be part of the project <project> to do list in the system 
    And the todo <title2> should not be part of the project <project> to do list in the system

    Examples: 
      | project   | title1    | title2    |
      | course1   | todo3     | todo4     |
      | course2   | todo3     | todo4     |
      
  Scenario Outline: Remove a todo from a non-existent course todo list (Error Flow)
    Given the todo <title> exists in the system
    And the project <project> does not exist in the system 
    When removing the todo <title> from the project <project> to do list
    Then an error not found message should be displayed

    Examples: 
      | title  | project  |
      | todo1  | course10 |
      | todo2  | course20 |