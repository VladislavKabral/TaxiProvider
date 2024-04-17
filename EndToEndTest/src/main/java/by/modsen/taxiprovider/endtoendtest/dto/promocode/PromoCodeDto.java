package by.modsen.taxiprovider.endtoendtest.dto.promocode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCodeDto {

    private long id;

    private String value;

    private double discount;
}
