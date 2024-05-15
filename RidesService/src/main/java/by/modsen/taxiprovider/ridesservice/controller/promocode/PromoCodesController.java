package by.modsen.taxiprovider.ridesservice.controller.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodesListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/promoCodes")
@RequiredArgsConstructor
public class PromoCodesController {

    private final PromoCodesService promoCodesService;

    @GetMapping
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<PromoCodesListDto> getPromoCodes() {
        return new ResponseEntity<>(promoCodesService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = "value")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<PromoCodeDto> getPromoCodeByValue(@RequestParam("value") String value)
            throws EntityNotFoundException {
        return new ResponseEntity<>(promoCodesService.findByValue(value), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoCodeResponseDto> savePromoCode(@RequestBody @Valid PromoCodeDto promoCodeDTO)
            throws EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(promoCodesService.save(promoCodeDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<PromoCodeResponseDto> editPromoCode(@PathVariable("id") long id,
                                                              @RequestBody @Valid PromoCodeDto promoCodeDTO)
            throws EntityValidateException {
        return new ResponseEntity<>(promoCodesService.update(id, promoCodeDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<PromoCodeResponseDto> deletePromoCode(@PathVariable("id") long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(promoCodesService.delete(id), HttpStatus.OK);
    }
}
