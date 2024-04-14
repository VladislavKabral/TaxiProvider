Feature: Passenger Service

  Scenario: Getting passengers when passengers exist
    Given The list of passengers
    When Get all passengers
    Then A list of passengers returned

  Scenario: Getting passenger when passenger exists
    Given The passenger
    When Find the passenger with id 1
    Then A response with passenger

  Scenario: Getting passenger when passenger doesn't exist
    Given The passenger with id 1 doesn't exist
    When Find the passenger with id 1
    Then The passenger with id 1 wasn't found

  Scenario: Saving new passenger when request is valid
    Given The request for saving new passenger
    When Save a new passenger
    Then A response with id of created passenger

  Scenario: Saving new passenger when request is invalid
    Given An invalid request for saving new passenger
    When Save a new passenger
    Then A new passenger wasn't created

  Scenario: Updating passenger when request is valid
    Given The valid request for updating passenger
    When Update the passenger
    Then A response with id of updated passenger

  Scenario: Updating passenger when request is invalid
    Given An invalid request for updating passenger
    When Update the passenger
    Then The passenger wasn't updated

  Scenario: Deactivating passenger when passenger exists
    Given The deactivating passenger
    When Deactivate the passenger with id 1
    Then A response with id of deactivated passenger

  Scenario: Getting passenger's profile when passenger exists
    Given The passenger with passenger's rating
    When Get profile of the passenger with id 1
    Then A response with profile of the passenger

  Scenario: Getting passenger's profile when passenger doesn't exists
    Given The passenger with id 1 doesn't exist
    When Get profile of the passenger with id 1
    Then The passenger with id 1 wasn't found
