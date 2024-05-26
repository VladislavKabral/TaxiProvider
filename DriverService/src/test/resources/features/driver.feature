Feature: Driver Service

  Scenario: Getting drivers when drivers exist
    Given The list of drivers
    When Get all drivers
    Then A list of drivers returned

  Scenario: Getting driver when driver exists
    Given The driver
    When Find the driver with id 1
    Then A response with driver

  Scenario: Getting driver when driver doesn't exist
    Given The driver with id 1 doesn't exist
    When Find the driver with id 1
    Then The driver with id 1 wasn't found

  Scenario: Saving new driver when request is valid
    Given The request for saving new driver
    When Save a new driver
    Then A response with id of created driver

  Scenario: Saving new driver when request is invalid
    Given An invalid request for saving new driver
    When Save a new driver
    Then A new driver wasn't created

  Scenario: Updating driver when request is valid
    Given The valid request for updating driver
    When Update the driver
    Then A response with id of updated driver

  Scenario: Updating driver when request is invalid
    Given An invalid request for updating driver
    When Update the driver
    Then The driver wasn't updated

  Scenario: Deactivating driver when driver exists
    Given The deactivating driver
    When Deactivate the driver with id 1
    Then A response with id of deactivated driver

  Scenario: Getting driver's profile when driver exists
    Given The driver with driver's rating
    When Get profile of the driver with id 1
    Then A response with profile of the driver

  Scenario: Getting driver's profile when driver doesn't exists
    Given The driver with id 1 doesn't exist
    When Get profile of the driver with id 1
    Then The driver with id 1 wasn't found
