package by.modsen.taxiprovider.passengerservice.unit;

import by.modsen.taxiprovider.passengerservice.controller.passenger.PassengersController;
import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDTO;
import by.modsen.taxiprovider.passengerservice.service.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.List;

import static by.modsen.taxiprovider.passengerservice.utility.PassengersTestUtil.*;
import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(PassengersController.class)
class PassengersControllerUnitTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PassengersService passengersService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testGetPassengersWhenPassengersExistReturnListOfPassengers() throws Exception {
		//given
		List<PassengerDTO> passengers = getPassengers();

		//when
		when(passengersService.findAll()).thenReturn(passengers);

		//then
		mockMvc.perform(get("/passengers")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].lastname").value(passengers.get(0).getLastname()))
				.andExpect(jsonPath("$[1].lastname").value(passengers.get(1).getLastname()))
				.andExpect(jsonPath("$[2].lastname").value(passengers.get(2).getLastname()));
	}

	@Test
	public void testGetSortedPassengersByLastnameWhenPassengersExistReturnListOfPassengers() throws Exception {
		//given
		List<PassengerDTO> sortedPassengers = getSortedPassengers();

		//when
		when(passengersService.findSortedPassengers(DEFAULT_SORT_FIELD)).thenReturn(sortedPassengers);

		//then
		mockMvc.perform(get("/passengers")
						.queryParam("sort", DEFAULT_SORT_FIELD)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].lastname").value(sortedPassengers.get(0).getLastname()))
				.andExpect(jsonPath("$[1].lastname").value(sortedPassengers.get(1).getLastname()))
				.andExpect(jsonPath("$[2].lastname").value(sortedPassengers.get(2).getLastname()));
	}

	@Test
	public void testGetPassengersWhenPassengersDoNotExistReturnErrorResponse() throws Exception {
		//given

		//when
		when(passengersService.findAll()).thenThrow(new EntityNotFoundException(PASSENGERS_NOT_FOUND));

		//then
		mockMvc.perform(get("/passengers")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(PASSENGERS_NOT_FOUND));
	}

	@Test
	public void testGetPassengerWhenPassengerExistsReturnPassengerWithGivenId() throws Exception {
		//given
		PassengerDTO passenger = getPassenger();

		//when
		when(passengersService.findById(DEFAULT_PASSENGER_ID)).thenReturn(passenger);

		//then
		mockMvc.perform(get("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(passenger.getId()))
				.andExpect(jsonPath("$.lastname").value(passenger.getLastname()))
				.andExpect(jsonPath("$.firstname").value(passenger.getFirstname()))
				.andExpect(jsonPath("$.email").value(passenger.getEmail()))
				.andExpect(jsonPath("$.phoneNumber").value(passenger.getPhoneNumber()));
	}

	@Test
	public void testGetPassengerWhenPassengerWithGivenIdWasNotFoundReturnErrorResponse() throws Exception {
		//given

		//when
		when(passengersService.findById(DEFAULT_PASSENGER_ID))
				.thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));

		//then
		mockMvc.perform(get("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));
	}

	@Test
	public void testGetPassengerPageWhenPageAndSizeAreValidReturnPassengersPage() throws Exception {
		//given
		Page<PassengerDTO> passengers = new PageImpl<>(getPassengers());

		//when
		when(passengersService.findPagePassengers(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD))
				.thenReturn(passengers);

		//then
		mockMvc.perform(get("/passengers")
						.queryParam("page", String.valueOf(DEFAULT_PAGE))
						.queryParam("size", String.valueOf(DEFAULT_PAGE_SIZE))
						.queryParam("sort", DEFAULT_SORT_FIELD)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(3))
				.andExpect(jsonPath("$.content[0].lastname").value(passengers.getContent().get(0).getLastname()))
				.andExpect(jsonPath("$.content[1].lastname").value(passengers.getContent().get(1).getLastname()))
				.andExpect(jsonPath("$.content[2].lastname").value(passengers.getContent().get(2).getLastname()));
	}

	@Test
	public void testGetPassengerPageWhenThereAreNotAnyPassengersOnThePageReturnErrorResponse() throws Exception {
		//given

		//when
		when(passengersService.findPagePassengers(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD))
				.thenThrow(new EntityNotFoundException(PASSENGERS_ON_PAGE_NOT_FOUND));

		//then
		mockMvc.perform(get("/passengers")
						.queryParam("page", String.valueOf(DEFAULT_PAGE))
						.queryParam("size", String.valueOf(DEFAULT_PAGE_SIZE))
						.queryParam("sort", DEFAULT_SORT_FIELD)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(PASSENGERS_ON_PAGE_NOT_FOUND));
	}

	@Test
	public void testGetPassengersPageWhenPageAndSizeAreInvalidReturnErrorResponse() throws Exception {
		//given

		//when
		when(passengersService.findPagePassengers(DEFAULT_INVALID_PAGE, DEFAULT_INVALID_PAGE_SIZE, DEFAULT_SORT_FIELD))
				.thenThrow(new InvalidRequestDataException(INVALID_PAGE_REQUEST));

		//then
		mockMvc.perform(get("/passengers")
						.queryParam("page", String.valueOf(DEFAULT_INVALID_PAGE))
						.queryParam("size", String.valueOf(DEFAULT_INVALID_PAGE_SIZE))
						.queryParam("sort", DEFAULT_SORT_FIELD)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(INVALID_PAGE_REQUEST));
	}

	@Test
	public void testGetPassengerProfileWhenPassengerExistsReturnPassengerProfile() throws Exception {
		//given
		PassengerProfileDTO passengerProfile = getPassengerProfile();

		//when
		when(passengersService.getPassengerProfile(DEFAULT_PASSENGER_ID)).thenReturn(passengerProfile);

		//then
		mockMvc.perform(get("/passengers/1/profile")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.passenger.lastname").value(passengerProfile.getPassenger().getLastname()))
				.andExpect(jsonPath("$.passenger.firstname").value(passengerProfile.getPassenger().getFirstname()))
				.andExpect(jsonPath("$.passenger.email").value(passengerProfile.getPassenger().getEmail()))
				.andExpect(jsonPath("$.rating").value(passengerProfile.getRating()));
	}

	@Test
	public void testGetPassengerProfileWhenPassengersProfileWasNotFoundReturnErrorResponse() throws Exception {
		//given

		//when
		when(passengersService.getPassengerProfile(DEFAULT_PASSENGER_ID))
				.thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));

		//then
		mockMvc.perform(get("/passengers/1/profile")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));
	}

	@Test
	public void testSavePassengerWhenRequestIsValidReturnIdOfCreatedPassenger() throws Exception {
		//given
		NewPassengerDTO newPassengerRequest = getRequestForSavePassenger();

		//when
		when(passengersService.save(eq(newPassengerRequest), any(BindingResult.class)))
				.thenReturn(new PassengerResponseDTO(DEFAULT_PASSENGER_ID));

		//then
		mockMvc.perform(post("/passengers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPassengerRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(DEFAULT_PASSENGER_ID));
	}

	@Test
	public void testSavePassengerWhenLastnameIsInvalidReturnErrorResponse() throws Exception {
		//given
		NewPassengerDTO newPassengerRequest = getRequestForSavePassengerWithInvalidLastName();

		//when
		when(passengersService.save(eq(newPassengerRequest), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_LASTNAME_BODY_IS_INVALID));

		//then
		mockMvc.perform(post("/passengers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPassengerRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_LASTNAME_BODY_IS_INVALID));
	}

	@Test
	public void testSavePassengerWhenFirstnameIsInvalidReturnErrorResponse() throws Exception {
		//given
		NewPassengerDTO newPassengerRequest = getRequestForSavePassengerWithInvalidFirstName();

		//when
		when(passengersService.save(eq(newPassengerRequest), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_FIRSTNAME_BODY_IS_INVALID));

		//then
		mockMvc.perform(post("/passengers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPassengerRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_FIRSTNAME_BODY_IS_INVALID));
	}

	@Test
	public void testSavePassengerWhenEmailIsInvalidReturnErrorResponse() throws Exception {
		//given
		NewPassengerDTO newPassengerRequest = getRequestForSavePassengerWithInvalidEmail();

		//when
		when(passengersService.save(eq(newPassengerRequest), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_EMAIL_WRONG_FORMAT));

		//then
		mockMvc.perform(post("/passengers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPassengerRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_EMAIL_WRONG_FORMAT));
	}

	@Test
	public void testSavePassengerWhenPhoneNumberIsInvalidReturnErrorResponse() throws Exception {
		//given
		NewPassengerDTO newPassengerRequest = getRequestForSavePassengerWithInvalidPhoneNumber();

		//when
		when(passengersService.save(eq(newPassengerRequest), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG));

		//then
		mockMvc.perform(post("/passengers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPassengerRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG));
	}

	@Test
	public void testSavePassengerWhenPasswordIsInvalidReturnErrorResponse() throws Exception {
		//given
		NewPassengerDTO newPassengerRequest = getRequestForSavePassengerWithInvalidPassword();

		//when
		when(passengersService.save(eq(newPassengerRequest), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_PASSWORD_IS_EMPTY));

		//then
		mockMvc.perform(post("/passengers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPassengerRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_PASSWORD_IS_EMPTY));
	}

	@Test
	public void testEditPassengerWhenRequestIsValidReturnIdOfUpdatedPassenger() throws Exception {
		//given
		PassengerDTO passengerDTO = getRequestForEditPassenger();

		//when
		when(passengersService.update(eq(DEFAULT_PASSENGER_ID), eq(passengerDTO), any(BindingResult.class)))
				.thenReturn(new PassengerResponseDTO(DEFAULT_PASSENGER_ID));

		//then
		mockMvc.perform(patch("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passengerDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(DEFAULT_PASSENGER_ID));
	}

	@Test
	public void testEditPassengerWhenPassengerWasNotFoundReturnErrorResponse() throws Exception {
		//given
		PassengerDTO passengerDTO = getRequestForEditPassenger();

		//when
		when(passengersService.update(eq(DEFAULT_PASSENGER_ID), eq(passengerDTO), any(BindingResult.class)))
				.thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));

		//then
		mockMvc.perform(patch("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passengerDTO)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));
	}

	@Test
	public void testEditPassengerWhenLastnameIsInvalidReturnErrorResponse() throws Exception {
		//given
		PassengerDTO passengerDTO = getRequestForEditPassengerWithInvalidLastname();

		//when
		when(passengersService.update(eq(DEFAULT_PASSENGER_ID), eq(passengerDTO), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_LASTNAME_BODY_IS_INVALID));

		//then
		mockMvc.perform(patch("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passengerDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_LASTNAME_BODY_IS_INVALID));
	}

	@Test
	public void testEditPassengerWhenFirstnameIsInvalidReturnErrorResponse() throws Exception {
		//given
		PassengerDTO passengerDTO = getRequestForEditPassengerWithInvalidFirstname();

		//when
		when(passengersService.update(eq(DEFAULT_PASSENGER_ID), eq(passengerDTO), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_FIRSTNAME_BODY_IS_INVALID));

		//then
		mockMvc.perform(patch("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passengerDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_FIRSTNAME_BODY_IS_INVALID));
	}

	@Test
	public void testEditPassengerWhenEmailIsInvalidReturnErrorResponse() throws Exception {
		//given
		PassengerDTO passengerDTO = getRequestForEditPassengerWithInvalidEmail();

		//when
		when(passengersService.update(eq(DEFAULT_PASSENGER_ID), eq(passengerDTO), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_EMAIL_WRONG_FORMAT));

		//then
		mockMvc.perform(patch("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passengerDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_EMAIL_WRONG_FORMAT));
	}

	@Test
	public void testEditPassengerWhenPhoneNumberIsInvalidReturnErrorResponse() throws Exception {
		//given
		PassengerDTO passengerDTO = getRequestForEditPassengerWithInvalidPhoneNumber();

		//when
		when(passengersService.update(eq(DEFAULT_PASSENGER_ID), eq(passengerDTO), any(BindingResult.class)))
				.thenThrow(new EntityValidateException(PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG));

		//then
		mockMvc.perform(patch("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passengerDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG));
	}

	@Test
	public void testDeactivatePassengerWhenPassengerExistsReturnIdOfDeactivatedPassenger() throws Exception {
		//given

		//when
		when(passengersService.deactivate(DEFAULT_PASSENGER_ID)).thenReturn(new PassengerResponseDTO(DEFAULT_PASSENGER_ID));

		//then
		mockMvc.perform(delete("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(DEFAULT_PASSENGER_ID));
	}

	@Test
	public void testDeactivatePassengerWhenPassengerWasNotFoundReturnErrorResponse() throws Exception {
		//given

		//when
		when(passengersService.deactivate(DEFAULT_PASSENGER_ID))
				.thenThrow(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));

		//then
		mockMvc.perform(delete("/passengers/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(String.format(PASSENGER_NOT_FOUND, DEFAULT_PASSENGER_ID)));
	}
}
