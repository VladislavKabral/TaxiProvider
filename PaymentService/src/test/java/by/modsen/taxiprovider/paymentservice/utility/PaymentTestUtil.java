package by.modsen.taxiprovider.paymentservice.utility;

import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.BalanceResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class PaymentTestUtil {

    public final String DEFAULT_CARD_NUMBER = "4242424242424242";
    public final int DEFAULT_EXPIRATION_MONTH = 10;
    public final int DEFAULT_EXPIRATION_YEAR = 2027;
    public final int DEFAULT_CVC = 202;
    public final int DEFAULT_PASSENGER_ID = 1;
    public final int DEFAULT_DRIVER_ID = 1;
    public final String DEFAULT_CURRENCY = "USD";
    public final BigDecimal DEFAULT_CHARGE_AMOUNT = BigDecimal.valueOf(10.0);
    public final String DEFAULT_CARD_TOKEN = "cur_fewfwfwfwfw23fds";
    public final String DEFAULT_CHARGE_STATUS = "SUCCEED";
    public final String DEFAULT_CUSTOMER_ID = "wqjhfqkjfhqf63jkbn";
    public final String DEFAULT_CUSTOMER_NAME = "Ivan Ivanov";
    public final String DEFAULT_CUSTOMER_EMAIL = "mr.ivanov@mail.ru";
    public final String DEFAULT_CUSTOMER_PHONE_NUMBER = "+375291234567";
    public final BigDecimal DEFAULT_CUSTOMER_BALANCE = BigDecimal.valueOf(100.0);
    public final String DEFAULT_CUSTOMER_ROLE = "PASSENGER";
    //TODO: Change exception's message
    public final String CUSTOMER_WAS_NOT_FOUND_MESSAGE = "";

    public CardRequestDTO getCardRequest() {
        return CardRequestDTO.builder()
                .number(DEFAULT_CARD_NUMBER)
                .month(DEFAULT_EXPIRATION_MONTH)
                .year(DEFAULT_EXPIRATION_YEAR)
                .cvc(DEFAULT_CVC)
                .build();
    }

    public CardRequestDTO getInvalidCardRequest() {
        return CardRequestDTO.builder()
                .number(DEFAULT_CARD_NUMBER)
                .month(DEFAULT_EXPIRATION_MONTH)
                .year(2020)
                .cvc(DEFAULT_CVC)
                .build();
    }

    public ChargeRequestDTO getChargeRequest() {
        return ChargeRequestDTO.builder()
                .amount(DEFAULT_CHARGE_AMOUNT)
                .cardToken(DEFAULT_CARD_TOKEN)
                .currency(DEFAULT_CURRENCY)
                .driverId(DEFAULT_DRIVER_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .build();
    }

    public ChargeRequestDTO getInvalidChargeRequest() {
        return ChargeRequestDTO.builder()
                .amount(BigDecimal.valueOf(-1.0))
                .cardToken(DEFAULT_CARD_TOKEN)
                .currency(DEFAULT_CURRENCY)
                .driverId(DEFAULT_DRIVER_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .build();
    }

    public ChargeResponseDTO getChargeResponse() {
        return ChargeResponseDTO.builder()
                .amount(DEFAULT_CHARGE_AMOUNT)
                .currency(DEFAULT_CURRENCY)
                .status(DEFAULT_CHARGE_STATUS)
                .build();
    }

    public CustomerResponseDTO getCustomerResponse() {
        return new CustomerResponseDTO(DEFAULT_CUSTOMER_ID);
    }

    public CustomerDTO getRequestForCustomer() {
        return CustomerDTO.builder()
                .name(DEFAULT_CUSTOMER_NAME)
                .email(DEFAULT_CUSTOMER_EMAIL)
                .phone(DEFAULT_CUSTOMER_PHONE_NUMBER)
                .taxiUserId(DEFAULT_PASSENGER_ID)
                .balance(DEFAULT_CUSTOMER_BALANCE)
                .role(DEFAULT_CUSTOMER_ROLE)
                .build();
    }

    public CustomerDTO getInvalidRequestForCustomer() {
        return CustomerDTO.builder()
                .name(DEFAULT_CUSTOMER_NAME)
                .email("mr.ivanovmail.ru")
                .phone(DEFAULT_CUSTOMER_PHONE_NUMBER)
                .taxiUserId(DEFAULT_PASSENGER_ID)
                .balance(DEFAULT_CUSTOMER_BALANCE)
                .role(DEFAULT_CUSTOMER_ROLE)
                .build();
    }

    public BalanceResponseDTO getBalanceResponse() {
        return BalanceResponseDTO.builder()
                .amount(DEFAULT_CHARGE_AMOUNT)
                .currency(DEFAULT_CURRENCY)
                .build();
    }

    public CustomerChargeRequestDTO getRequestForChargeByCustomer() {
        return CustomerChargeRequestDTO.builder()
                .taxiUserId(DEFAULT_PASSENGER_ID)
                .amount(DEFAULT_CHARGE_AMOUNT)
                .currency(DEFAULT_CURRENCY)
                .role(DEFAULT_CUSTOMER_ROLE)
                .build();
    }

    public CustomerChargeRequestDTO getInvalidRequestForChargeByCustomer() {
        return CustomerChargeRequestDTO.builder()
                .taxiUserId(DEFAULT_PASSENGER_ID)
                .amount(DEFAULT_CHARGE_AMOUNT)
                .currency(DEFAULT_CURRENCY)
                .role(null)
                .build();
    }
}
