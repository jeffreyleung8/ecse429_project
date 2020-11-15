Feature: Create a to do list for a new class
  As a student, I create a to do list for a new class I am taking, so I can manage course work.

  Background:
    Given system is ready
    And the following classes are created in the system:
      | title   | completed   | active	 | description              |
      | COMP417 | false       | true     | Intro to Robotics        |
      | COMP424 | false       | false    | Artificial Intelligence  |
      | COMP551 | true        | false    | Machine Learning         |
      | ECSE429 | true        | false    | Software Validation      |


  Scenario Outline: Create a new class todo list with a title (Normal Flow)
    Given <title> is the title of the new todo
    When a student sends a todo post request
    Then a new todo instance should be created

    Examples:
      | title         |
      | Assignment1   |
      | Assignment2   |

  Scenario Outline: Create a new class todo list with a title, status and description  (Alt Flow)
    Given <title> is the title of the new todo
    And <doneStatus> is the done status of the new todo
    And <description> is the description of the new todo
    When a student sends a todo post request
    Then a new todo instance should be created

    Examples:
      | title      | doneStatus | description  |
      | Midterm1   | false      | Chap1-5      |
      | Midterm2   | false      | Chap1-7      |

  Scenario Outline: Create a new class todo list without a title (Error Flow)
    Given <doneStatus> is the done status of the new todo
    And <description> is the description of the new todo
    When a student sends a todo post request
    Then an error message "Todo instance not created" should be displayed

    Examples:
      | doneStatus | description |
      | false      | desc3       |
      | false      | desc4       |