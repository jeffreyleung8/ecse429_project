Feature: Add task to course to do list
  As a student, I add a task to a course to do list, so I can remember it.

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
	
  Scenario Outline: Add a todo to a course todo list (Normal Flow)
    Given the todo <title> exists in the system
    And the project <project> exists in the system
    When adding the todo <title> to the project <project> to do list
    Then the todo <title> should be part of the project <project> to do list in the system 

    Examples: 
      | project   | title     | 
      | course1   | todo1     | 
      | course2   | todo2     |     
      
  Scenario Outline: Add multiple todos to the same course todo list (Alt Flow)
    Given the todo <title1> exists in the system
    And the todo <title2> exists in the system
    And the project <project> exists in the system
    When adding the todo <title1> to the project <project> to do list
    And adding the todo <title2> to the project <project> to do list
    Then the todo <title1> should be part of the project <project> to do list in the system 
    And the todo <title2> should be part of the project <project> to do list in the system

    Examples: 
      | project   | title1    | title2    |
      | course1   | todo1     | todo2     |
      | course2   | todo3     | todo4     |
      
  Scenario Outline: Add a todo to a non-existent course todo list (Error Flow)
    Given the todo <title> exists in the system
    And the project <project> does not exist in the system 
    When adding the todo <title> to the project <project> to do list
    Then a bad request message should be displayed

    Examples: 
      | title  | project  |
      | todo1  | course10 |
      | todo2  | course20 |