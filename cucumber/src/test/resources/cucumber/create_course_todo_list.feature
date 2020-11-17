Feature: Create a to do list for a new class
  As a student, I create a to do list for a new class I am taking, so I can manage course work.

  Background:
    Given system is ready

  Scenario Outline: Create a new class todo list (project) (Normal Flow)
    Given the course with title <title> does not exist in the system
    When a student requests to create a new course with title <title>
    Then a new course instance with title <title> should be created

    Examples:
      | title   |
      | COMP417 |
      | ECSE429 |

  Scenario Outline: Create a new class todo list (project) with title, status and description  (Alt Flow)
    Given the course with title <title> does not exist in the system
    And <completed> is the completed status of the new course
    And <active> is the active status of the new course
    And <description> is the description of the new course
    When a student requests to create a new course with title <title>
    Then a new course instance with <title>, <active>, <completed> and <description> should be created

    Examples:
      | title   | completed   | active	 | description       |
      | COMP202 | false       | true     | Programming       |
      | COMP310 | false       | false    | OperatingSystem   |

  Scenario Outline: Create a new class todo list (project) with invalid active status (Error Flow)
    Given the course with title <title> does not exist in the system
    And <active> is the active status of the new course
    When a student requests to create a new course with title <title>
    Then an error message "Invalid active status" should be displayed

    Examples:
      | title   | active  |
      | COMP420 | Yes     |
      | COMP551 | No      |