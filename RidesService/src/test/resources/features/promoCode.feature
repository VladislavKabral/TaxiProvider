Feature: PromoCodes Service

  Scenario: Getting promo codes when promo codes exist
    Given The list of promo codes
    When Get all promo codes
    Then A list of promo codes returned

  Scenario: Getting promo codes when promo codes don't exist
    Given The empty list of promo codes
    When Get all promo codes
    Then An empty list of promo codes returned

  Scenario: Getting promo code by value when promo code exists
    Given The promo code
    When Get promo codes with value "JAVA"
    Then A response with the promo code

  Scenario: Getting promo code by value when promo code doesn't exist
    Given The promo code doesn't exist
    When Get promo code with value "Sfewf"
    Then A response with error

  Scenario: Saving promo code when request is valid
    Given The valid save promo code request
    When Save promo code
    Then A response with promo code's id

  Scenario: Saving promo code when request is invalid
    Given The invalid save promo code request
    When Save promo code
    Then A response with error

  Scenario: Updating promo code when request is valid
    Given The valid update promo code request
    When Update promo code
    Then A response with promo code's id

  Scenario: Updating promo code when request is invalid
    Given The invalid update promo code request
    When Update promo code
    Then A response with error

  Scenario: Deleting promo code when promo code exists
    Given The deleting promo code
    When Delete promo code with id 1
    Then A response with promo code's id

  Scenario: Deleting promo code when promo code doesn't exist
    Given The deleting promo code doesn't exist
    When Delete promo code with id 6
    Then A response with error