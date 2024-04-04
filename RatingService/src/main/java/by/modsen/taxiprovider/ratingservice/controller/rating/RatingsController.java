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
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<TaxiUserRatingDto> getTaxiUserRating(@RequestParam("taxiUserId")  long taxiUserId,
                                                               @RequestParam("role") String role)
            throws EntityNotFoundException {
        TaxiUserRequestDto requestDTO = new TaxiUserRequestDto(taxiUserId, role);
        return new ResponseEntity<>(ratingsService.getTaxiUserRating(requestDTO), HttpStatus.OK);
    }

    @PostMapping("/init")
    public ResponseEntity<RatingResponseDto> initTaxiUser(@RequestBody @Valid TaxiUserRequestDto requestDTO,
                                                          BindingResult bindingResult) throws EntityValidateException {
        return new ResponseEntity<>(ratingsService.initTaxiUserRatings(requestDTO, bindingResult), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<RatingResponseDto> rateTaxiUser(@RequestBody @Valid RatingDto ratingDTO,
                                                          BindingResult bindingResult) throws EntityValidateException {
        return new ResponseEntity<>(ratingsService.save(ratingDTO, bindingResult), HttpStatus.CREATED);
    }
}
