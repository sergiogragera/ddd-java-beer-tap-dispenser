Feature: User can open a dispenser when dispenser is closed
  Scenario: User open a closed dispenser
    Given the dispenser exists
      And the user open a dispenser
     Then the user receives status code of 202

  Scenario: User open an opened dispenser
    Given the dispenser exists
      And the user open a dispenser
      And the dispenser is successfully opened
      And the user open a dispenser
     Then the user receives status code of 409

  Scenario: User open a closed dispenser before close
    Given the dispenser exists
      And the user open a dispenser
      And the user close a dispenser
      And the user open a dispenser 1 second ago
     Then the user receives status code of 409
    
  Scenario: User open a closed dispenser after usage
    Given the dispenser exists
      And the user open a dispenser
      And the dispenser is successfully opened
      And the user close a dispenser
      And the dispenser is successfully closed
      And the user open a dispenser
     Then the user receives status code of 202