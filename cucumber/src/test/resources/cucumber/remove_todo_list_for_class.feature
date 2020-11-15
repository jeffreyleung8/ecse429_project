Feature: Remove a to do list for a class
  As a student, I remove a to do list for a class which I am no longer taking, to declutter my schedule.

  Background:
    Given system is ready
    And the following classes (projects) are created in the system:
      | title   | completed   | active	 | description              |
      | COMP417 | false       | true     | Intro to Robotics        |
      | COMP424 | false       | false    | Artificial Intelligence  |
      | COMP551 | true        | false    | Machine Learning         |
    And the following todos are created in the system:
      | title         | doneStatus | description  |
      | Assignment1   | false      | Chap1-2      |
      | Assignment2   | false      | Chap3-4      |
      | Assignment3   | false      | Chap5-6      |
      | Assignment4   | false      | Chap7-8      |
    And the following todos are associated to course 'COMP417'
      | title         | doneStatus | description  |
      | Assignment1   | false      | Chap1-2      |
      | Assignment2   | false      | Chap3-4      |
    And the following todos are associated to course 'COMP424'
      | title         | doneStatus | description  |
      | Assignment3   | false      | Chap5-6      |
      | Assignment4   | false      | Chap7-8      |
    And the following todos are associated to course 'COMP551'
      | title         | doneStatus | description  |
      | Assignment1   | false      | Chap1-2      |
      | Assignment2   | false      | Chap3-4      |
      | Assignment3   | false      | Chap5-6      |
    And the class 'ECSE999' does not exist in the system
    And the class 'COMP999' does not exist in the system

  Scenario Outline: Remove a valid todo list for a class (Normal Flow)
    Given the class <classTitle> exists in the system
    And the todo <todoTitle> exists in the system
    And <todoTitle> is a todo list for the class <classTitle>
    When a student requests to remove the todo list <todoTitle> for this class <classTitle>
    Then the todo list <todoTitle> should not be part of the class <classTitle> in the system

    Examples:
      | classTitle | todoTitle     |
      | COMP417    | Assignment1   |
      | COMP417    | Assignment2   |

  Scenario Outline: Remove all todo lists for a class  (Alt Flow)
    Given the class <classTitle> exists in the system
    And the class <classTitle> has <n> todo lists
    When a student requests to remove all <n> todo lists for this class <classTitle>
    Then all todo lists should be removed for the class <classTitle> in the system

    Examples:
      | classTitle  | n   |
      | COMP424     | 2   |
      | COMP551     | 3   |

  Scenario Outline: Remove a todo list for a non-existent class(Error Flow)
    Given the todo <todoTitle> exists in the system
    And the class <classTitle> does not exist in the system
    When a student requests to remove the todo list <todoTitle> for this class <classTitle>
    Then an error "Todo not removed" should be displayed

    Examples:
      | classTitle | todoTitle     |
      | ECSE999    | Assignment1   |
      | COMP999    | Assignment2   |