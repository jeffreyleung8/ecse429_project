Feature: Remove a to do list for a class
  As a student, I remove a todo list for a class which I am no longer taking, to declutter my schedule.

  Background:
    Given system is ready

  Scenario Outline: Remove a valid course todo list with more than one task (project)(Normal Flow)
    Given the following classes are created in the system:
      | title   | completed   | active	 | description     |
      | <title> | false       | true     | <description>   |
    And the following todos are created in the system:
      | title         | doneStatus | description  |
      | Assignment1   | false      | Chap1-2      |
      | Assignment2   | false      | Chap3-4      |
      | Assignment3   | false      | Chap5-6      |
    And the following todos are associated to course <title>
      | title         | doneStatus | description  |
      | Assignment1   | false      | Chap1-2      |
      | Assignment2   | false      | Chap3-4      |
      | Assignment3   | false      | Chap5-6      |
    And the course with title <title> has <n> todos
    When a student requests to remove the course with title <title>
    Then the course with title <title> should be removed from the system

    Examples:
      | title   | description             | n  |
      | COMP424 | ArtificialIntelligence  | 3  |
      | COMP551 | MachineLearning         | 3  |

  Scenario Outline: Remove an empty course todo list (project) (Alt Flow)
    Given the following classes are created in the system:
      | title   | completed   | active	 | description     |
      | <title> | false       | true     | <description>   |
    And the course with title <title> has an empty todo list
    When a student requests to remove the course with title <title>
    Then the course with title <title> should be removed from the system

    Examples:
      | title   | description     |
      | COMP251 | Algorithms      |
      | MATH240 | DiscreteMath    |

  Scenario Outline: Remove a course todo list (project) that does not exist (Error Flow)
    Given the course with title <title> does not exist in the system
    When a student requests to remove the course with title <title>
    Then an error "Course does not exist" should be displayed

    Examples:
      | title    |
      | COMP999  |
      | ECSE999  |