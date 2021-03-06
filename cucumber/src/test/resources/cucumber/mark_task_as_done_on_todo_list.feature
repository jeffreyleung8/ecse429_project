Feature: Mark task as done on course to do list
  As a student, I mark a task as done on my course to do list, so I can track my accomplishments.

  Background: 
    Given system is ready
    And the following todos are created in the system:
    	| title | doneStatus  | description |
      | task1 | false       | test1       |
      | task2 | false       | test2       |
      | task3 | true        | test3       |
      | task4 | true        | test4       |
	
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
      
      
   
