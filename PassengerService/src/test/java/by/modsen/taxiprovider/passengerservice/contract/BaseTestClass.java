package by.modsen.taxiprovider.passengerservice.contract;

import by.modsen.taxiprovider.passengerservice.PassengerServiceApplication;
import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDto;
import by.modsen.taxiprovider.passengerservice.service.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.utility.PassengersTestUtil.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PassengerServiceApplication.class)
public class BaseTestClass {

    @MockBean
    private PassengersService passengersService;

    @Autowired
    protected WebApplicationContext context;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        when(passengersService.findAll()).thenReturn(getPassengersListDto());

        when(passengersService.findById(DEFAULT_PASSENGER_ID)).thenReturn(getPassenger());

        NewPassengerDto newDriverRequest = getRequestForSavePassenger();
        when(passengersService.save(newDriverRequest))
                .thenReturn(new PassengerResponseDto(DEFAULT_INVALID_PASSENGER_ID));

        PassengerDto passengerDTO = getPassenger();
        when(passengersService.update(DEFAULT_PASSENGER_ID, passengerDTO))
                .thenReturn(new PassengerResponseDto(DEFAULT_PASSENGER_ID));

        when(passengersService.deactivate(DEFAULT_PASSENGER_ID))
                .thenReturn(new PassengerResponseDto(DEFAULT_PASSENGER_ID));

        PassengerProfileDto passengerProfile = getPassengerProfile();
        when(passengersService.getPassengerProfile(DEFAULT_PASSENGER_ID)).thenReturn(passengerProfile);

        when(passengersService.deactivate(DEFAULT_INVALID_PASSENGER_ID))
                .thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_INVALID_PASSENGER_ID)));

        when(passengersService.getPassengerProfile(DEFAULT_INVALID_PASSENGER_ID))
                .thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_INVALID_PASSENGER_ID)));

        when(passengersService.findById(DEFAULT_INVALID_PASSENGER_ID))
                .thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_INVALID_PASSENGER_ID)));

        RestAssuredMockMvc.webAppContextSetup(this.context);
    }
}
