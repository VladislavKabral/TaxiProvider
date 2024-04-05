package by.modsen.taxiprovider.ridesservice.unit;

import by.modsen.taxiprovider.ridesservice.controller.promocode.PromoCodesController;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodesListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
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

import static by.modsen.taxiprovider.ridesservice.utility.PromoCodesTestUtil.*;
import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(PromoCodesController.class)
public class PromoCodesControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromoCodesService promoCodesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetPromoCodesWhenPromoCodesExistReturnListOfPromoCodes() throws Exception {
        //given
        PromoCodesListDto promoCodes = new PromoCodesListDto(getPromoCodes());

        //when
        when(promoCodesService.findAll()).thenReturn(promoCodes);

        //then
        mockMvc.perform(get("/promoCodes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].value").value(promoCodes.getContent().get(0).getValue()))
                .andExpect(jsonPath("$.content[1].value").value(promoCodes.getContent().get(1).getValue()))
                .andExpect(jsonPath("$.content[2].value").value(promoCodes.getContent().get(2).getValue()));
    }

    @Test
    public void testGetPromoCodesWhenPromoCodesDoNotExistReturnErrorResponse() throws Exception {
        //given
        PromoCodesListDto promoCodes = getEmptyPromoCodesList();

        //when
        when(promoCodesService.findAll()).thenReturn(promoCodes);

        //then
        mockMvc.perform(get("/promoCodes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void testGetPromoCodeByValueWhenPromoCodeExistsReturnPromoCode() throws Exception {
        //given
        PromoCodeDto promoCode = getPromoCode();

        //when
        when(promoCodesService.findByValue(DEFAULT_PROMO_CODE_VALUE)).thenReturn(promoCode);

        //then
        mockMvc.perform(get("/promoCodes")
                        .queryParam("value", DEFAULT_PROMO_CODE_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(promoCode.getId()))
                .andExpect(jsonPath("$.value").value(promoCode.getValue()))
                .andExpect(jsonPath("$.discount").value(promoCode.getDiscount()));
    }

    @Test
    public void testGetPromoCodeByValueWhenPromoCodeDoesNotExistReturnErrorResponse() throws Exception {
        //given

        //when
        when(promoCodesService.findByValue(DEFAULT_PROMO_CODE_VALUE))
                .thenThrow(new EntityNotFoundException(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, DEFAULT_PROMO_CODE_VALUE)));

        //then
        mockMvc.perform(get("/promoCodes")
                        .queryParam("value", DEFAULT_PROMO_CODE_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, DEFAULT_PROMO_CODE_VALUE)));
    }

    @Test
    public void testSavePromoCodeWhenRequestIsValidReturnIdOfCreatedPromoCode() throws Exception {
        //given
        PromoCodeDto request = getRequestForSavePromoCode();
        PromoCodeResponseDto response = getResponse();

        //when
        when(promoCodesService.save(request)).thenReturn(response);

        //then
        mockMvc.perform(post("/promoCodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(DEFAULT_PROMO_CODE_ID));
    }

    @Test
    public void testSavePromoCodeWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        PromoCodeDto request = getInvalidRequestForSavePromoCode();

        //when
        when(promoCodesService.save(request))
                .thenThrow(new EntityValidateException(PROMO_CODE_SIZE_IS_INVALID));

        //then
        mockMvc.perform(post("/promoCodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PROMO_CODE_SIZE_IS_INVALID));
    }

    @Test
    public void testEditPromoCodeWhenRequestIsValidReturnIdOfUpdatedPromoCode() throws Exception {
        //given
        PromoCodeDto request = getRequestForEditPromoCode();
        PromoCodeResponseDto response = getResponse();

        //when
        when(promoCodesService.update(request.getId(), request)).thenReturn(response);

        //then
        mockMvc.perform(patch("/promoCodes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    public void testEditPromoCodeWhenRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        PromoCodeDto request = getInvalidRequestForEditPromoCode();

        //when
        when(promoCodesService.update(request.getId(), request))
                .thenThrow(new EntityValidateException(DISCOUNT_IS_INVALID));

        //then
        mockMvc.perform(patch("/promoCodes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DISCOUNT_IS_INVALID));
    }

    @Test
    public void testDeletePromoCodeWhenPromoCodeExistsReturnIdOfDeletedPromoCode() throws Exception {
        //given
        PromoCodeResponseDto response = getResponse();

        //when
        when(promoCodesService.delete(DEFAULT_PROMO_CODE_ID)).thenReturn(response);

        //then
        mockMvc.perform(delete("/promoCodes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    public void testDeletePromoCodeWhenPromoCodeDoesNotExistReturnErrorResponse() throws Exception {
        //given

        //when
        when(promoCodesService.delete(DEFAULT_PROMO_CODE_ID))
                .thenThrow(new EntityNotFoundException(String.format(PROMO_CODE_WITH_ID_NOT_FOUND, DEFAULT_PROMO_CODE_ID)));

        //then
        mockMvc.perform(delete("/promoCodes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(PROMO_CODE_WITH_ID_NOT_FOUND, DEFAULT_PROMO_CODE_ID)));
    }
}
