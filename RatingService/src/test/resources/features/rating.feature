Feature: Rating Service

  Scenario: Getting rating of the driver
    Given The request for getting driver's rating
    When Get a rating of the driver with id 1
    Then A response with rating of the driver

  Scenario: Getting rating of the passenger
    Given The request for getting passenger's rating
    When Get a rating of the passenger with id 1
    Then A response with rating of the passenger

  Scenario: Getting rating of the driver when driver doesn't exist
    Given The request for getting non-existing driver's rating
    When Get a rating of the driver with id 4
    Then Driver with id 4 wasn't found

  Scenario: Getting rating of the passenger when passenger doesn't exist
    Given The request for getting non-existing passenger's rating
    When Get a rating of the passenger with id 4
    Then Passenger with id 4 wasn't found

  Scenario: Initialization of driver's rating
    Given The request for initialization of driver's rating
    When Init driver's rating
    Then Response with driver's rating

  Scenario: Initialization of passenger's rating
    Given The request for initialization of passenger's rating
    When Init passenger's rating
    Then Response with passenger's rating

  Scenario: Rating the driver
    Given The request for rating the driver
    When Rate the driver
    Then Response with not default driver's rating

  Scenario: Rating the passenger
    Given The request for rating the passenger
    When Rate the passenger
    Then Response with not default passenger's rating

  Scenario: Rating the driver with invalid request
    Given The invalid request for rating the driver
    When Rate the driver
    Then Response with error of validation driver's request

  Scenario: Rating the passenger with invalid request
    Given The invalid request for rating the passenger
    When Rate the passenger
    Then Response with error of validation passenger's request