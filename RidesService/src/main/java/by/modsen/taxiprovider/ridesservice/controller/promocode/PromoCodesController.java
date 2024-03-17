package by.modsen.taxiprovider.ridesservice.controller.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDTO;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<PromoCodeResponseDTO> savePromoCode(@RequestBody @Valid PromoCodeDTO promoCodeDTO,
                                                              BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(promoCodesService.save(promoCodeDTO, bindingResult), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PromoCodeResponseDTO> editPromoCode(@PathVariable("id") long id,
                                                    @RequestBody @Valid PromoCodeDTO promoCodeDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {
        return new ResponseEntity<>(promoCodesService.update(id, promoCodeDTO, bindingResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PromoCodeResponseDTO> deletePromoCode(@PathVariable("id") long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(promoCodesService.delete(id), HttpStatus.OK);
    }
}
