Feature: Add task to course to do list
  As a student, I add a task to a course to do list, so I can remember it.

  Background: 
    Given system is ready
    And the following projects are created in the system:
      | title   | completed   | active	 | description |
      | class1  | false       | true     | desc1       |
      | class2  | false       | false    | desc2       |
      | class3  | true        | false    | desc3       |
      | class4  | true        | false    | desc4       |
    And the following todos are created in the system:
      | title   | doneStatus  | description |
      | hwk1    | false       | test1       |
      | hwk2    | true        | test2       |
      | hwk3    | false       | test3       |
      | hwk4    | true        | test4       |
      | hwk5    | false       | test3       |
      | hwk6    | true        | test4       |
	
  Scenario Outline: Add a todo to a course todo list (Normal Flow)
    Given the todo <title> exists in the system
    And the project <project> exists in the system
    When adding the todo <title> to the project <project> to do list
    Then the todo <title> should be part of the project <project> to do list in the system 

    Examples: 
      | project   | title     | 
      | class1    | hwk1      |
      | class2    | hwk2      |
      
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
      | class3    | hwk5      | hwk6      |
      | class4    | hwk3      | hwk4      |
      
  Scenario Outline: Add a todo to a non-existent course todo list (Error Flow)
    Given the todo <title> exists in the system
    And the project <project> does not exist in the system 
    When adding the todo <title> to the project <project> to do list
    Then an error not found message should be displayed

    Examples: 
      | title  | project  |
      | hwk1   | class10  |
      | hwk2   | class20  |