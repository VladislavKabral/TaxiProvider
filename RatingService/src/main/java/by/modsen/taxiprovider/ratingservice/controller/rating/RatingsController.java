package by.modsen.taxiprovider.ratingservice.controller.rating;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.ratingservice.service.RatingsService;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ratings")
public class RatingsController {

    private final RatingsService ratingsService;

    @GetMapping(params = {"taxiUserId", "role"})
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<TaxiUserRatingDto> getTaxiUserRating(@RequestParam("taxiUserId")  long taxiUserId,
                                                               @RequestParam("role") String role)
            throws EntityNotFoundException {
        TaxiUserRequestDto requestDTO = new TaxiUserRequestDto(taxiUserId, role);
        return new ResponseEntity<>(ratingsService.getTaxiUserRating(requestDTO), HttpStatus.OK);
    }

    @PostMapping("/init")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RatingResponseDto> initTaxiUser(@RequestBody @Valid TaxiUserRequestDto requestDTO)
            throws EntityValidateException {
        return new ResponseEntity<>(ratingsService.initTaxiUserRatings(requestDTO), HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<RatingResponseDto> rateTaxiUser(@RequestBody @Valid RatingDto ratingDTO)
            throws EntityValidateException {
        return new ResponseEntity<>(ratingsService.save(ratingDTO), HttpStatus.CREATED);
    }
}
