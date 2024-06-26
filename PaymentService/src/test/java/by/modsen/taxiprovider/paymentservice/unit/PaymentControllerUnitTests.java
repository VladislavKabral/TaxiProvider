package by.modsen.taxiprovider.paymentservice.unit;

import by.modsen.taxiprovider.paymentservice.controller.payment.PaymentController;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDto;
import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.response.BalanceResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDto;
import by.modsen.taxiprovider.paymentservice.service.payment.PaymentService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static by.modsen.taxiprovider.paymentservice.utility.PaymentTestUtil.*;
import static by.modsen.taxiprovider.paymentservice.util.Message.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(PaymentController.class)
public class PaymentControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetTokenWhenRequestIsValidReturnToken() throws Exception {
        //given
        CardRequestDto request = getCardRequest();
        TokenResponseDto response = new TokenResponseDto(DEFAULT_CARD_TOKEN);

        //when
        when(paymentService.createStripeToken(request)).thenReturn(response);

        //then
        mockMvc.perform(post("/payment/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.getToken()));
    }

    @Test
    public void testGetTokenWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        CardRequestDto request = getInvalidCardRequest();

        //when
        when(paymentService.createStripeToken(request))
                .thenThrow(new EntityValidateException(BANK_CARD_EXPIRATION_IS_INVALID));

        //then
        mockMvc.perform(post("/payment/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BANK_CARD_EXPIRATION_IS_INVALID));
    }

    @Test
    public void testChargeWhenRequestIsValidReturnChargeResponse() throws Exception {
        //given
        ChargeRequestDto request = getChargeRequest();
        ChargeResponseDto response = getChargeResponse();

        //when
        when(paymentService.charge(request)).thenReturn(response);

        //then
        mockMvc.perform(post("/payment/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(response.getAmount()))
                .andExpect(jsonPath("$.currency").value(response.getCurrency()))
                .andExpect(jsonPath("$.status").value(response.getStatus()));
    }

    @Test
    public void testChargeWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        ChargeRequestDto request = getInvalidChargeRequest();

        //when

        //then
        mockMvc.perform(post("/payment/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(AMOUNT_MINIMAL_VALUE_IS_INVALID));
    }

    @Test
    public void testSaveCustomerWhenRequestIsValidReturnIdOfCreatedCustomer() throws Exception {
        //given
        CustomerDto request = getRequestForCustomer();
        CustomerResponseDto response = getCustomerResponse();

        //when
        when(paymentService.createCustomer(request)).thenReturn(response);

        //then
        mockMvc.perform(post("/payment/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    public void testSaveCustomerWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        CustomerDto request = getInvalidRequestForCustomer();

        //when
        when(paymentService.createCustomer(request))
                .thenThrow(new EntityValidateException(CUSTOMER_EMAIL_IS_INVALID));

        //then
        mockMvc.perform(post("/payment/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(CUSTOMER_EMAIL_IS_INVALID));
    }

    @Test
    public void testEditCustomerWhenRequestIsValidReturnIdOfUpdatedCustomer() throws Exception {
        //given
        CustomerDto request = getRequestForCustomer();
        CustomerResponseDto response = getCustomerResponse();

        //when
        when(paymentService.updateCustomer(request.getTaxiUserId(), request))
                .thenReturn(response);

        //then
        mockMvc.perform(patch("/payment/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    public void testEditCustomerWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        CustomerDto request = getInvalidRequestForCustomer();

        //when

        //then
        mockMvc.perform(patch("/payment/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(CUSTOMER_EMAIL_IS_INVALID));
    }

    @Test
    public void testUpdateDriverBalanceWhenRequestIsValidReturnIdOfDriver() throws Exception {
        //given
        CustomerResponseDto response = getCustomerResponse();

        //when
        when(paymentService.updateDriverBalance(DEFAULT_DRIVER_ID)).thenReturn(response);

        //then
        mockMvc.perform(patch("/payment/customers/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    public void testUpdateDriverBalanceWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given

        //when
        when(paymentService.updateDriverBalance(DEFAULT_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND, DEFAULT_DRIVER_ID, "DRIVER")));

        //then
        mockMvc.perform(patch("/payment/customers/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(USER_NOT_FOUND, DEFAULT_DRIVER_ID, "DRIVER")));
    }

    @Test
    public void testGetCustomerBalanceWhenCustomerExistsReturnBalanceOfCustomer() throws Exception {
        //given
        BalanceResponseDto response = getBalanceResponse();

        //when
        when(paymentService.getCustomerBalance(DEFAULT_CUSTOMER_ID)).thenReturn(response);

        //then
        mockMvc.perform(get("/payment/customers/wqjhfqkjfhqf63jkbn/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(response.getAmount()))
                .andExpect(jsonPath("$.currency").value(response.getCurrency()));
    }

    @Test
    public void testGetCustomerBalanceWhenCustomerDoesNotExistReturnErrorResponse() throws Exception {
        //given

        //when
        when(paymentService.getCustomerBalance(DEFAULT_CUSTOMER_ID))
                .thenThrow(new PaymentException(String.format(CUSTOMER_WAS_NOT_FOUND_MESSAGE, DEFAULT_CUSTOMER_ID)));

        //then
        mockMvc.perform(get("/payment/customers/wqjhfqkjfhqf63jkbn/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format(CUSTOMER_WAS_NOT_FOUND_MESSAGE, DEFAULT_CUSTOMER_ID)));
    }

    @Test
    public void testDeleteCustomerWhenCustomerExistsReturnIdOfDeletedCustomer() throws Exception {
        //given
        CustomerResponseDto response = getCustomerResponse();

        //when
        when(paymentService.deleteCustomer(DEFAULT_PASSENGER_ID, DEFAULT_CUSTOMER_ROLE)).thenReturn(response);

        //then
        mockMvc.perform(delete("/payment/customers/1")
                        .queryParam("role", DEFAULT_CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    public void testDeleteCustomerWhenCustomerDoesNotExistReturnErrorResponse() throws Exception {
        //given

        //when
        when(paymentService.deleteCustomer(DEFAULT_PASSENGER_ID, DEFAULT_CUSTOMER_ROLE))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND, DEFAULT_PASSENGER_ID, DEFAULT_CUSTOMER_ROLE)));

        //then
        mockMvc.perform(delete("/payment/customers/1")
                        .queryParam("role", DEFAULT_CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(USER_NOT_FOUND, DEFAULT_PASSENGER_ID, DEFAULT_CUSTOMER_ROLE)));
    }

    @Test
    public void testChargeByCustomerWhenRequestIsValidReturnChargeResponse() throws Exception {
        //given
        CustomerChargeRequestDto request = getRequestForChargeByCustomer();
        ChargeResponseDto response = getChargeResponse();

        //when
        when(paymentService.chargeFromCustomer(request)).thenReturn(response);

        //then
        mockMvc.perform(post("/payment/customerCharge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(response.getStatus()))
                .andExpect(jsonPath("$.amount").value(response.getAmount()))
                .andExpect(jsonPath("$.currency").value(response.getCurrency()));
    }

    @Test
    public void testChargeByCustomerWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        CustomerChargeRequestDto request = getInvalidRequestForChargeByCustomer();

        //when

        //then
        mockMvc.perform(post("/payment/customerCharge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(TAXI_USER_ROLE_IS_EMPTY));
    }
}
