Feature: Driver Service

  Scenario: Getting drivers when drivers exist
    Given The list of drivers
    When The method findAll is called
    Then A list of drivers returned

  Scenario: Getting driver when driver exists
    Given The driver
    When The method findById is called with id 1
    Then A driver response with driver

  Scenario: Getting driver when driver doesn't exist
    Given The driver with id 1 doesn't exist
    When The method findById is called with id 1
    Then EntityNotFoundException is threw and response doesn't have driver with id 1

  Scenario: Saving new driver when request is valid
    Given The request for saving new driver
    When The method save is called
    Then DriverResponse with id of created driver

  Scenario: Saving new driver when request is invalid
    Given An invalid request for saving new driver
    When The method save is called
    Then EntityValidateException is threw

  Scenario: Updating driver when request is valid
    Given The valid request for updating driver
    When The method update is called
    Then Driver response with id of updated driver

  Scenario: Updating driver when request is invalid
    Given An invalid request for updating driver
    When The method update is called
    Then EntityValidateException is threw

  Scenario: Deactivating driver when driver exists
    Given The deactivating driver
    When The method deactivate is called with id 1
    Then Driver response with id of deactivated driver

  Scenario: Getting driver's profile when driver exists
    Given The driver with driver's rating
    When The method getProfile is called with id 1
    Then Response with profile of driver

  Scenario: Getting driver's profile when driver doesn't exists
    Given The driver with id 1 doesn't exist
    When The method getProfile is called with id 1
    Then EntityNotFoundException is threw and response doesn't have driver with id 1
