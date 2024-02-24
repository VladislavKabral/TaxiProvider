package by.modsen.taxiprovider.ridesservice.controller.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.validation.promocode.PromoCodeValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promoCodes")
public class PromoCodesController {

    private final PromoCodesService promoCodesService;

    private final PromoCodeMapper promoCodeMapper;

    private final PromoCodeValidator promoCodeValidator;

    @Autowired
    public PromoCodesController(PromoCodesService promoCodesService,
                                PromoCodeMapper promoCodeMapper,
                                PromoCodeValidator promoCodeValidator) {
        this.promoCodesService = promoCodesService;
        this.promoCodeMapper = promoCodeMapper;
        this.promoCodeValidator = promoCodeValidator;
    }

    @GetMapping
    public ResponseEntity<List<PromoCodeDTO>> getPromoCodes() throws EntityNotFoundException {
        return new ResponseEntity<>(promoCodeMapper.toListDTO(promoCodesService.findAll()),
                HttpStatus.OK);
    }

    @GetMapping(path = "/promoCode", params = "value")
    public ResponseEntity<PromoCodeDTO> getPromoCodeByValue(@RequestParam("value") String value)
            throws EntityNotFoundException {

        return new ResponseEntity<>(promoCodeMapper.toDTO(promoCodesService.findByValue(value)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> savePromoCode(@RequestBody @Valid PromoCodeDTO promoCodeDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {

        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCodeValidator.validate(promoCode, bindingResult);
        handleBindingResult(bindingResult);
        promoCodesService.save(promoCode);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPromoCode(@PathVariable("id") long id,
                                                    @RequestBody @Valid PromoCodeDTO promoCodeDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {

        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCode.setId(id);
        promoCodeValidator.validate(promoCode, bindingResult);
        handleBindingResult(bindingResult);
        promoCodesService.update(id, promoCode);

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
