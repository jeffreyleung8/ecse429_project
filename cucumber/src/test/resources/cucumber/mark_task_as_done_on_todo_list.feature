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
    And the todo <title> is not marked as done
    When I set the task <title> as done
    Then the todo <title> will be set as done in the system

    Examples: 
      | title | doneStatus  | 
      | task1 | true        | 
      | task2 | true        | 
      
      
  Scenario Outline: Mark task that is completed as done (Alt Flow)
    Given the todo <title> exists in the system
    And the todo <title> is marked as done
    When I set the task <title> as done
    Then the todo <title> in the system will not be modified

    Examples: 
      | title | doneStatus  | 
      | task3 | true        | 
      | task4 | true        | 
      
      
   
