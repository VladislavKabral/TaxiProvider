package by.modsen.taxiprovider.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private String name;

    private String email;

    private String phone;

    private long passengerId;

    private BigDecimal balance;
}
