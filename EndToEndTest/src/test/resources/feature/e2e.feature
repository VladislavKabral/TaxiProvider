Feature: End-to-end tests
  Scenario: A passenger books a ride
    Given A request for booking new ride
    When A passenger send request for booking new ride
    Then A response with id of created ride
    And A driver should be taken

  Scenario: A driver starts a ride
    Given A request for starting a ride
    When A driver starts a ride
    Then A response of the started ride

  Scenario: A driver finishes a ride
    Given A request for finish a ride
    When A driver finishes a ride
    Then A response of completed ride
    And A driver should be free

  Scenario: A passenger books a new ride
    Given A request for booking new ride
    When A passenger send request for booking new ride
    Then A response with id of created ride
    And A driver should be taken

  Scenario: A passenger cancels a ride
    Given A request for cancel a ride
    When A passenger cancels a ride
    Then A response with cancelled ride


  Scenario: A driver views his ride's history
    Given A request for getting driver ride's history
    When A driver get his ride's history
    Then A list of rides

  Scenario: A passenger views his ride's history
    Given A request for getting passenger ride's history
    When A passenger get his ride's history
    Then A list of rides

  Scenario: A passenger views his profile
    Given A request for getting passenger's profile
    When A passenger get his profile
    Then A passenger's profile

  Scenario: A driver views his profile
    Given A request for getting driver's profile
    When A driver get his profile
    Then A driver's profile