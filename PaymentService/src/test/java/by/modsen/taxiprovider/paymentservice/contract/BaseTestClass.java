package by.modsen.taxiprovider.paymentservice.contract;

import by.modsen.taxiprovider.paymentservice.PaymentServiceApplication;
import by.modsen.taxiprovider.paymentservice.service.payment.PaymentService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import static by.modsen.taxiprovider.paymentservice.utility.PaymentTestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PaymentServiceApplication.class)
public class BaseTestClass {

    @MockBean
    private PaymentService paymentService;

    @Autowired
    protected WebApplicationContext context;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        when(paymentService.charge(any())).thenReturn(getChargeResponse());

        when(paymentService.createStripeToken(any())).thenReturn(getTokenResponse());

        when(paymentService.createCustomer(any())).thenReturn(getCustomerResponse());

        when(paymentService.updateCustomer(anyLong(), any())).thenReturn(getCustomerResponse());

        when(paymentService.deleteCustomer(anyLong(), anyString())).thenReturn(getCustomerResponse());

        when(paymentService.getCustomerBalance(anyString())).thenReturn(getCustomerBalance());

        when(paymentService.updateDriverBalance(anyLong())).thenReturn(getCustomerResponse());

        when(paymentService.chargeFromCustomer(any())).thenReturn(getChargeResponse());

        RestAssuredMockMvc.webAppContextSetup(this.context);
    }
}
