package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBalanceRequestDTO {

    @NotBlank(message = CUSTOMER_ID_IS_EMPTY)
    private String customerId;
}
