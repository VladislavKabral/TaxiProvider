Feature: Rides Service

  Scenario: Getting rides when rides exist
    Given The list of rides
    When Get all rides
    Then A list of rides returned

  Scenario: Getting rides when rides don't exist
    Given The empty list of rides
    When Get all rides
    Then An empty list of rides returned

  Scenario: Getting passenger's rides when rides exist
    Given The list of passenger's rides
    When Get rides of the passenger with id 1
    Then A list of passenger's rides returned

  Scenario: Getting passenger's rides when rides don't exist
    Given The empty list of passenger's rides
    When Get rides of the passenger with id 4
    Then An empty list of passenger's rides returned

  Scenario: Getting driver's rides when rides exist
    Given The list of driver's rides
    When Get rides of the driver with id 1
    Then A list of driver's rides returned

  Scenario: Getting driver's rides when rides don't exist
    Given The empty list of driver's rides
    When Get rides of the driver with id 4
    Then An empty list of driver's rides returned

  Scenario: Getting ride by id when ride exists
    Given The ride
    When Get ride with id 1
    Then A response with the ride

  Scenario: Getting ride by id when ride doesn't exist
    Given The ride doesn't exist
    When Get ride with id 6
    Then A response with error message

  Scenario: Saving ride when request is valid
    Given The valid save request
    When Save ride
    Then A response with ride's id

  Scenario: Saving ride when request is invalid
    Given The invalid save request
    When Save ride
    Then A response with error message

  Scenario: Updating ride when request is valid
    Given The valid update request
    When Update ride
    Then A response with ride's id

  Scenario: Updating ride when request is invalid
    Given The invalid update request
    When Update ride
    Then A response with error message

  Scenario: Cancelling ride when ride exists
    Given The cancelling ride
    When Cancel ride
    Then A response with ride's id

  Scenario: Cancelling ride when ride doesn't exist
    Given The cancelling ride doesn't exist
    When Cancel ride
    Then A response with error message