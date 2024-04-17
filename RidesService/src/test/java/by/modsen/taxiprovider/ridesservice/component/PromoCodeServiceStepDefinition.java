package by.modsen.taxiprovider.ridesservice.component;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodesListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.validation.promocode.PromoCodeValidator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.utility.PromoCodesTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class PromoCodeServiceStepDefinition {

    @Mock
    private PromoCodesRepository promoCodesRepository;

    @Mock
    private PromoCodeMapper promoCodeMapper;

    @Mock
    private PromoCodeValidator promoCodeValidator;

    @InjectMocks
    private PromoCodesService promoCodesService;

    private PromoCodeResponseDto promoCodeResponse;

    private PromoCodeDto promoCode;

    private PromoCodesListDto promoCodesListResponse;

    private Exception exception;

    private PromoCodeDto request;

    private String expectedMessage;

    private long promoCodeId;

    public PromoCodeServiceStepDefinition() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("The list of promo codes")
    public void theListOfPromoCodes() {
        when(promoCodesRepository.findAll()).thenReturn(getPromoCodesList());

        when(promoCodeMapper.toListDto(any())).thenReturn(getPromoCodes());
    }

    @When("Get all promo codes")
    public void getAllPromoCodes() {
        promoCodesListResponse = promoCodesService.findAll();
    }

    @Then("A list of promo codes returned")
    public void aListOfPromoCodesReturned() {
        assertEquals(3, promoCodesListResponse.getContent().size());
    }

    @Given("The empty list of promo codes")
    public void theEmptyListOfPromoCodes() {
        when(promoCodesRepository.findAll()).thenReturn(getPromoCodesList());

        when(promoCodeMapper.toListDto(any())).thenReturn(getEmptyPromoCodeDtoList().getContent());
    }

    @Then("An empty list of promo codes returned")
    public void anEmptyListOfPromoCodesReturned() {
        assertEquals(0, promoCodesListResponse.getContent().size());
    }

    @Given("The promo code")
    public void thePromoCode() {
        when(promoCodesRepository.findByValue(DEFAULT_PROMO_CODE_VALUE)).thenReturn(Optional.ofNullable(getPromoCode()));

        when(promoCodeMapper.toDto(any())).thenReturn(getPromoCodeDto());
    }

    @When("Get promo codes with value {string}")
    public void getPromoCodesByValue(String value) {
        try {
            promoCode = promoCodesService.findByValue(value);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with the promo code")
    public void aResponseWithThePromoCode() {
        assertEquals(DEFAULT_PROMO_CODE_ID, promoCode.getId());
        assertEquals(DEFAULT_PROMO_CODE_VALUE, promoCode.getValue());
        assertEquals(DEFAULT_PROMO_CODE_DISCOUNT, promoCode.getDiscount());
    }

    @Given("The promo code doesn't exist")
    public void thePromoCodeDoesNotExist() {
        when(promoCodesRepository.findByValue(NON_EXISTS_PROMO_CODE_VALUE)).thenReturn(Optional.empty());
    }

    @When("Get promo code with value {string}")
    public void getPromoCodeByValue(String value) {
        expectedMessage = String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, NON_EXISTS_PROMO_CODE_VALUE);
        try {
            promoCode = promoCodesService.findByValue(value);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with error")
    public void aResponseWithError() {
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("The valid save promo code request")
    public void theValidSavePromoCodeRequest() {
        request = getRequestForSavePromoCode();
        PromoCode newPromoCode = getNewPromoCode();

        when(promoCodeMapper.toEntity(request)).thenReturn(newPromoCode);

        when(promoCodesRepository.findByValue(newPromoCode.getValue())).thenReturn(Optional.of(newPromoCode));
    }

    @When("Save promo code")
    public void savePromoCode() {
        expectedMessage = String.format(PROMO_CODE_ALREADY_EXISTS, NON_EXISTS_PROMO_CODE_VALUE);
        promoCodeId = NON_EXISTS_PROMO_CODE_ID;
        try {
            promoCodeResponse = promoCodesService.save(request);
        } catch (EntityValidateException | EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with promo code's id")
    public void aResponseWithPromoCodeSId() {
        assertEquals(promoCodeId, promoCodeResponse.getId());
    }

    @Given("The invalid save promo code request")
    public void theInvalidSavePromoCodeRequest() throws EntityValidateException {
        request = getRequestForSavePromoCode();
        PromoCode newPromoCode = getNewPromoCode();

        when(promoCodeMapper.toEntity(request)).thenReturn(newPromoCode);

        doThrow(new EntityValidateException(String.format(PROMO_CODE_ALREADY_EXISTS, newPromoCode.getValue())))
                .when(promoCodeValidator)
                .validate(newPromoCode);
    }

    @Given("The valid update promo code request")
    public void theValidUpdatePromoCodeRequest() {
        request = getPromoCodeDto();

        when(promoCodeMapper.toEntity(request)).thenReturn(getPromoCode());
    }

    @When("Update promo code")
    public void updatePromoCode() {
        expectedMessage = DISCOUNT_IS_INVALID;
        promoCodeId = DEFAULT_PROMO_CODE_ID;
        try {
            promoCodeResponse = promoCodesService.update(request.getId(), request);
        } catch (EntityValidateException e) {
            exception = e;
        }
    }

    @Given("The invalid update promo code request")
    public void theInvalidUpdatePromoCodeRequest() throws EntityValidateException {
        request = getInvalidRequestForEditPromoCode();
        PromoCode invalidPromoCode = getInvalidPromoCode();

        when(promoCodeMapper.toEntity(request)).thenReturn(invalidPromoCode);

        doThrow(new EntityValidateException(DISCOUNT_IS_INVALID))
                .when(promoCodeValidator)
                .validate(invalidPromoCode);
    }

    @Given("The deleting promo code")
    public void theDeletingPromoCode() {
        when(promoCodesRepository.findById(DEFAULT_PROMO_CODE_ID)).thenReturn(Optional.ofNullable(getPromoCode()));
    }

    @When("Delete promo code with id {long}")
    public void deletePromoCode(long id) {
        promoCodeId = DEFAULT_PROMO_CODE_ID;
        expectedMessage = String.format(PROMO_CODE_WITH_ID_NOT_FOUND, NON_EXISTS_PROMO_CODE_ID);
        try {
            promoCodeResponse = promoCodesService.delete(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Given("The deleting promo code doesn't exist")
    public void theDeletingPromoCodeDoesNotExist() {
        when(promoCodesRepository.findById(NON_EXISTS_PROMO_CODE_ID)).thenReturn(Optional.empty());
    }
}
