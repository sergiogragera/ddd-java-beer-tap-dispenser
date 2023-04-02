Feature: User can close a dispenser when dispenser is opened
  Scenario: User close an opened dispenser
    Given a dispenser exists
      And the user open a dispenser
      And the dispenser is successfully opened
      And the user close a dispenser
     Then the user receives status code of 202

  Scenario: User close a closed dispenser
    Given a dispenser exists
      And the user close a dispenser
     Then the user receives status code of 409

  Scenario: User close an opened dispenser before open
    Given a dispenser exists
      And the user open a dispenser
      And the user close a dispenser 1 second ago
     Then the user receives status code of 409
    
  Scenario: User close an opened dispenser after usage
    Given a dispenser exists
      And the user open a dispenser
      And the dispenser is successfully opened
      And the user close a dispenser
      And the dispenser is successfully closed
      And the user open a dispenser
      And the dispenser is successfully opened
      And the user close a dispenser
     Then the user receives status code of 202