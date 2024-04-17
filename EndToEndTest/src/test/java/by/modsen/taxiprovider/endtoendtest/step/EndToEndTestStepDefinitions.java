package by.modsen.taxiprovider.endtoendtest.step;

import by.modsen.taxiprovider.endtoendtest.client.DriverServiceClient;
import by.modsen.taxiprovider.endtoendtest.client.PassengerServiceClient;
import by.modsen.taxiprovider.endtoendtest.client.RidesServiceClient;
import by.modsen.taxiprovider.endtoendtest.dto.driver.DriverDto;
import by.modsen.taxiprovider.endtoendtest.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.endtoendtest.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.endtoendtest.dto.request.NewRideRequestDto;
import by.modsen.taxiprovider.endtoendtest.dto.response.RideResponseDto;
import by.modsen.taxiprovider.endtoendtest.dto.response.RidesListResponseDto;
import by.modsen.taxiprovider.endtoendtest.dto.ride.RideDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static by.modsen.taxiprovider.endtoendtest.util.EndToEntTestUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class EndToEndTestStepDefinitions {

    private final DriverServiceClient driverServiceClient;

    private final RidesServiceClient ridesServiceClient;

    private final PassengerServiceClient passengerServiceClient;

    private NewRideRequestDto newRideRequest;

    private RideDto ride;

    private RideResponseDto rideResponse;

    private RidesListResponseDto ridesHistory;

    private long rideId;

    private long passengerId;

    private long driverId;

    private DriverDto driver;

    private DriverProfileDto driverProfile;

    private PassengerProfileDto passengerProfile;

    @Given("A request for booking new ride")
    public void aRequestForBookingNewRide() {
        newRideRequest = getRequestForSaveRide();
    }

    @When("A passenger send request for booking new ride")
    public void aPassengerSendRequestForBookingNewRide() {
        rideResponse = ridesServiceClient.saveRide(newRideRequest).getBody();
        rideId = rideResponse.getId();
        passengerId = rideResponse.getPassengerId();
        driverId = rideResponse.getDriverId();
    }

    @Then("A response with id of created ride")
    public void aResponseWithIdOfCreatedRide() {
        assertThat(rideId).isNotNull();
        assertThat(rideId).isNotEqualTo(0);
        assertThat(passengerId).isNotNull();
        assertThat(passengerId).isNotEqualTo(0);
        assertThat(driverId).isNotNull();
        assertThat(driverId).isNotEqualTo(0);
    }

    @And("A driver should be taken")
    public void aDriverShouldBeTaken() {
        driver = driverServiceClient.getDriverById(driverId).getBody();
        assertThat(driver.getStatus()).isEqualTo(DRIVER_STATUS_TAKEN);
    }

    @Given("A request for starting a ride")
    public void aRequestForStartingARide() {
        ride = getRequestForStartRide();
    }

    @When("A driver starts a ride")
    public void aDriverStartsARide() {
        rideResponse = ridesServiceClient.updateRide(ride).getBody();
        rideId = rideResponse.getId();
    }

    @Then("A response of the started ride")
    public void aResponseOfTheStartedRide() {
        assertThat(rideId).isNotNull();
        assertThat(rideId).isNotEqualTo(0);
    }

    @Given("A request for finish a ride")
    public void aRequestForFinishARide() {
        ride = getRequestForEndRide();
    }

    @When("A driver finishes a ride")
    public void aDriverFinishesARide() {
        rideResponse = ridesServiceClient.updateRide(ride).getBody();
        rideId = rideResponse.getId();
    }

    @Then("A response of completed ride")
    public void aResponseOfCompletedRide() {
        assertThat(rideId).isNotNull();
        assertThat(rideId).isNotEqualTo(0);
    }

    @And("A driver should be free")
    public void aDriverShouldBeFree() {
        driver = driverServiceClient.getDriverById(rideResponse.getDriverId()).getBody();
        assertThat(driver.getStatus()).isEqualTo(DEFAULT_DRIVER_STATUS);
    }

    @Given("A request for cancel a ride")
    public void aRequestForCancelARide() {
        passengerId = DEFAULT_PASSENGER_ID;
    }

    @When("A passenger cancels a ride")
    public void aPassengerCancelsARide() {
        rideResponse = ridesServiceClient.cancelRide(passengerId).getBody();
        rideId = rideResponse.getId();
    }

    @Then("A response with cancelled ride")
    public void aResponseWithCancelledRide() {
        assertThat(rideId).isNotNull();
        assertThat(rideId).isNotEqualTo(0);
    }

    @Given("A request for getting driver ride's history")
    public void aRequestForGettingDriverSRideSHistory() {
        driverId = DEFAULT_DRIVER_ID;
    }

    @When("A driver get his ride's history")
    public void driverGetHisRideSHistory() {
        ridesHistory = ridesServiceClient.getDriverRides(driverId).getBody();
    }

    @Then("A list of rides")
    public void aListOfRides() {
        assertThat(ridesHistory.getContent().size()).isGreaterThan(0);
    }

    @Given("A request for getting passenger ride's history")
    public void aRequestForGettingPassengerRideSHistory() {
        passengerId = DEFAULT_PASSENGER_ID;
    }

    @When("A passenger get his ride's history")
    public void passengerGetHisRideSHistory() {
        ridesHistory = ridesServiceClient.getPassengerRides(passengerId).getBody();
    }

    @Given("A request for getting passenger's profile")
    public void aRequestForGettingPassengerSProfile() {
        passengerId = DEFAULT_PASSENGER_ID;
    }

    @When("A passenger get his profile")
    public void aPassengerGetHisProfile() {
        passengerProfile = passengerServiceClient.getPassengerProfile(passengerId).getBody();
    }

    @Then("A passenger's profile")
    public void aPassengerSProfile() {
        assertThat(passengerProfile).isNotNull();
        assertThat(passengerProfile.getPassenger()).isNotNull();
        assertThat(passengerProfile.getRating()).isGreaterThan(0);
    }

    @Given("A request for getting driver's profile")
    public void aRequestForGettingDriverSProfile() {
        driverId = DEFAULT_DRIVER_ID;
    }

    @When("A driver get his profile")
    public void aDriverGetHisProfile() {
        driverProfile = driverServiceClient.getDriverProfile(driverId).getBody();
    }

    @Then("A driver's profile")
    public void aDriverSProfile() {
        assertThat(driverProfile).isNotNull();
        assertThat(driverProfile.getDriver()).isNotNull();
        assertThat(driverProfile.getRating()).isGreaterThan(0);
    }
}
