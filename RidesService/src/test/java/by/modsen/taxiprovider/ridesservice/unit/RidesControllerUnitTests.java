package by.modsen.taxiprovider.ridesservice.unit;

import by.modsen.taxiprovider.ridesservice.controller.ride.RidesController;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.utility.RidesTestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(RidesController.class)
class RidesControllerUnitTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RidesService ridesService;

	@MockBean
	private PromoCodesService promoCodesService;

	private final ObjectMapper objectMapper = new ObjectMapper();


	@Test
	public void testGetRidesWhenRidesExistReturnListOfRides() throws Exception {
		//given
		List<RideDTO> rides = getRides();

		//when
		when(ridesService.findAll()).thenReturn(rides);

		//then
		mockMvc.perform(get("/rides")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(rides.get(0).getId()))
				.andExpect(jsonPath("$[0].passengerId").value(rides.get(0).getPassengerId()))
				.andExpect(jsonPath("$[0].driverId").value(rides.get(0).getDriverId()))
				.andExpect(jsonPath("$[0].cost").value(rides.get(0).getCost()))
				.andExpect(jsonPath("$[0].status").value(rides.get(0).getStatus()))
				.andExpect(jsonPath("$[0].paymentType").value(rides.get(0).getPaymentType()));
	}

	@Test
	public void testGetRidesWhenRidesDoNotExistReturnErrorResponse() throws Exception {
		//given

		//when
		when(ridesService.findAll()).thenThrow(new EntityNotFoundException(RIDES_NOT_FOUND));

		//then
		mockMvc.perform(get("/rides")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(RIDES_NOT_FOUND));
	}

	@Test
	public void testGetPassengerRidesWhenRidesExistReturnListOfRides() throws Exception {
		//given
		List<RideDTO> rides = getRides();

		//when
		when(ridesService.findByPassengerId(DEFAULT_PASSENGER_ID)).thenReturn(rides);

		//then
		mockMvc.perform(get("/rides/passenger/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(rides.get(0).getId()))
				.andExpect(jsonPath("$[0].passengerId").value(rides.get(0).getPassengerId()))
				.andExpect(jsonPath("$[0].driverId").value(rides.get(0).getDriverId()))
				.andExpect(jsonPath("$[0].cost").value(rides.get(0).getCost()))
				.andExpect(jsonPath("$[0].status").value(rides.get(0).getStatus()))
				.andExpect(jsonPath("$[0].paymentType").value(rides.get(0).getPaymentType()));
	}

	@Test
	public void testGetPassengerRidesWhenRidesDoNotExistReturnErrorResponse() throws Exception {
		//given

		//when
		when(ridesService.findByPassengerId(DEFAULT_PASSENGER_ID))
				.thenThrow(new EntityNotFoundException(String.format(PASSENGER_RIDES_NOT_FOUND, DEFAULT_PASSENGER_ID)));

		//then
		mockMvc.perform(get("/rides/passenger/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(PASSENGER_RIDES_NOT_FOUND, DEFAULT_PASSENGER_ID)));
	}

	@Test
	public void testDriverRidesWhenRidesExistReturnListOfRides() throws Exception {
		//given
		List<RideDTO> rides = getRides();

		//when
		when(ridesService.findByDriverId(DEFAULT_DRIVER_ID)).thenReturn(rides);

		//then
		mockMvc.perform(get("/rides/driver/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(rides.get(0).getId()))
				.andExpect(jsonPath("$[0].passengerId").value(rides.get(0).getPassengerId()))
				.andExpect(jsonPath("$[0].driverId").value(rides.get(0).getDriverId()))
				.andExpect(jsonPath("$[0].cost").value(rides.get(0).getCost()))
				.andExpect(jsonPath("$[0].status").value(rides.get(0).getStatus()))
				.andExpect(jsonPath("$[0].paymentType").value(rides.get(0).getPaymentType()));
	}

	@Test
	public void testGetDriverRidesWhenRidesDoNotExistReturnErrorResponse() throws Exception {
		//given

		//when
		when(ridesService.findByDriverId(DEFAULT_DRIVER_ID))
				.thenThrow(new EntityNotFoundException(String.format(DRIVER_RIDES_NOT_FOUND, DEFAULT_DRIVER_ID)));

		//then
		mockMvc.perform(get("/rides/driver/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(DRIVER_RIDES_NOT_FOUND, DEFAULT_DRIVER_ID)));
	}

	@Test
	public void testGetRideByIdWhenRideExistReturnRide() throws Exception {
		//given
		RideDTO ride = getRide();

		//when
		when(ridesService.findById(DEFAULT_RIDE_ID)).thenReturn(ride);

		//then
		mockMvc.perform(get("/rides/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ride.getId()))
				.andExpect(jsonPath("$.passengerId").value(ride.getPassengerId()))
				.andExpect(jsonPath("$.driverId").value(ride.getDriverId()))
				.andExpect(jsonPath("$.cost").value(ride.getCost()))
				.andExpect(jsonPath("$.status").value(ride.getStatus()))
				.andExpect(jsonPath("$.paymentType").value(ride.getPaymentType()));
	}

	@Test
	public void testGetRideByIdWhenRideDoesNotExistReturnErrorResponse() throws Exception {
		//given

		//when
		when(ridesService.findById(DEFAULT_RIDE_ID))
				.thenThrow(new EntityNotFoundException(String.format(RIDE_NOT_FOUND, DEFAULT_RIDE_ID)));

		//then
		mockMvc.perform(get("/rides/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(RIDE_NOT_FOUND, DEFAULT_RIDE_ID)));
	}

	@Test
	public void testSaveRideWhenRequestIsValidReturnIdOfCreatedRide() throws Exception {
		//given
		NewRideDTO request = getRequestForSaveRide();
		RideResponseDTO response = new RideResponseDTO(DEFAULT_RIDE_ID);

		//when
		when(ridesService.save(eq(request), any(BindingResult.class)))
				.thenReturn(response);

		//then
		mockMvc.perform(post("/rides")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(response.getId()));
	}

	@Test
	public void testSaveRideWhenRequestIsInvalidReturnErrorResponse() throws Exception {
		//given
		NewRideDTO request = getInvalidRequestForSaveRide();

		//when
		when(ridesService.save(eq(request), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(SOURCE_ADDRESS_IS_EMPTY));

		//then
		mockMvc.perform(post("/rides")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(SOURCE_ADDRESS_IS_EMPTY));
	}

	@Test
	public void testEditRideWhenRequestIsValidReturnIdOfUpdatedRide() throws Exception {
		//given
		RideDTO request = getRequestForEditDrive();
		RideResponseDTO response = getResponse();

		//when
		when(ridesService.update(eq(request), any(BindingResult.class))).thenReturn(response);

		//then
		mockMvc.perform(patch("/rides")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(response.getId()));
	}

	@Test
	public void testEditRideWhenRequestIsInvalidReturnErrorResponse() throws Exception {
		//given
		RideDTO request = getInvalidRequestForEditDrive();

		//when
		when(ridesService.update(eq(request), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(DESTINATION_ADDRESS_IS_EMPTY));

		//then
		mockMvc.perform(patch("/rides")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(DESTINATION_ADDRESS_IS_EMPTY));
	}

	@Test
	public void testCancelRideWhenRideExistsReturnIdOfCancelledRide() throws Exception {
		//given
		RideResponseDTO response = new RideResponseDTO(DEFAULT_RIDE_ID);

		//when
		when(ridesService.cancel(DEFAULT_PASSENGER_ID)).thenReturn(response);

		//then
		mockMvc.perform(patch("/rides/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(DEFAULT_RIDE_ID));
	}

	@Test
	public void testCancelRideWhenRideDoesNotExistReturnErrorResponse() throws Exception {
		//given

		//when
		when(ridesService.cancel(DEFAULT_PASSENGER_ID)).thenThrow(new EntityNotFoundException(WAITING_RIDES_NOT_FOUND));

		//then
		mockMvc.perform(patch("/rides/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(WAITING_RIDES_NOT_FOUND));
	}
}
