package by.modsen.taxiprovider.ridesservice.controller.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.validation.promocode.PromoCodeValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/promoCodes")
@RequiredArgsConstructor
public class PromoCodesController {

    private final PromoCodesService promoCodesService;

    private final PromoCodeValidator promoCodeValidator;

    @GetMapping
    public ResponseEntity<List<PromoCodeDTO>> getPromoCodes() throws EntityNotFoundException {
        return new ResponseEntity<>(promoCodesService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = "value")
    public ResponseEntity<PromoCodeDTO> getPromoCodeByValue(@RequestParam("value") String value)
            throws EntityNotFoundException {

        return new ResponseEntity<>(promoCodesService.findByValue(value), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> savePromoCode(@RequestBody @Valid PromoCodeDTO promoCodeDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {

        promoCodeValidator.validate(promoCodeDTO, bindingResult);
        handleBindingResult(bindingResult);
        promoCodesService.save(promoCodeDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPromoCode(@PathVariable("id") long id,
                                                    @RequestBody @Valid PromoCodeDTO promoCodeDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {

        promoCodeValidator.validate(promoCodeDTO, bindingResult);
        handleBindingResult(bindingResult);
        promoCodesService.update(id, promoCodeDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePromoCode(@PathVariable("id") long id) throws EntityNotFoundException {
        promoCodesService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handleBindingResult(BindingResult bindingResult) throws EntityValidateException {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();

            for (FieldError error: bindingResult.getFieldErrors()) {
                message.append(error.getDefaultMessage()).append(". ");
            }

            throw new EntityValidateException(message.toString());
        }
    }
}
