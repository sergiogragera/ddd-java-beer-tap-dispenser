Feature: The user wants to retrieve usages of dispenser
  Scenario: User retrieve dispenser without usages
    Given a dispenser exists
     When the user gets the created dispenser spendings
     Then the user receives status code of 200
      And the total amount is equals to 0

  Scenario: User retrieve opened dispenser
    Given a dispenser exists
      And the user open a dispenser 10 seconds ago
     When the user gets the created dispenser spendings
     Then the user receives status code of 200
      And the total amount is greater than 0

  Scenario: User retrieve closed dispenser
    Given a dispenser exists
      And the user open a dispenser 10 seconds ago
      And the user close a dispenser
     When the user gets the created dispenser spendings
     Then the user receives status code of 200
      And the total amount is greater than 0
      And exists 1 usage

   Scenario: User retrieve opened dispenser closed ago
    Given a dispenser exists
      And the user open a dispenser 10 seconds ago
      And the user close a dispenser 5 seconds ago
      And the user open a dispenser
     When the user gets the created dispenser spendings
     Then the user receives status code of 200
      And the total amount is greater than 0
      And exists 2 usages

  Scenario: User retrieve closed dispenser multiple times
    Given a dispenser exists
      And the user open a dispenser 10 seconds ago
      And the user close a dispenser 7 seconds ago
      And the user open a dispenser 5 seconds ago
      And the user close a dispenser
     When the user gets the created dispenser spendings
     Then the user receives status code of 200
      And the total amount is greater than 0
      And exists 2 usages