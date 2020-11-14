Feature: Add task to course to do list
  As a student, I add a task to a course to do list, so I can remember it.

  Background: 
    Given system is ready
    And the following projects are created in the system:
    	| title | completed   | active	 | description |
      | proj1 | false       | true     | desc1       |
      | proj2 | false       | false    | desc2       |
      | proj3 | true        | false    | desc3       |
      | proj4 | true        | false    | desc4       |
	
  Scenario Outline: Mark task that is not completed as done (Normal Flow)
    Given the todo <title> exists in the system
    And the todo <title> is marked incomplete
    When marking the todo <title> as completed
    Then the todo <title> should be marked completed in the system

    Examples: 
      | title | doneStatus  | 
      | task1 | true        | 
      | task2 | true        |     
      
  Scenario Outline: Mark task that is completed as done (Alt Flow)
    Given the todo <title> exists in the system
    And the todo <title> is marked completed
    When marking the todo <title> as completed
    Then the todo <title> should be marked completed in the system

    Examples: 
      | title | doneStatus  | 
      | task3 | true        | 
      | task4 | true        | 
      
  Scenario Outline: Mark non-existent task as done (Error Flow)
    Given the todo <title> does not exist in the system
    When marking the todo <title> as completed
    Then an error not found message should be displayed

    Examples: 
      | title  | 
      | task10 | 
      | task20 | 