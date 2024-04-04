package by.modsen.taxiprovider.ratingservice.controller.rating;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDTO;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDTO;
import by.modsen.taxiprovider.ratingservice.service.RatingsService;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TaxiUserRatingDTO> getTaxiUserRating(@RequestParam("taxiUserId")  long taxiUserId,
                                                               @RequestParam("role") String role)
            throws EntityNotFoundException {
        TaxiUserRequestDTO requestDTO = new TaxiUserRequestDTO(taxiUserId, role);
        return new ResponseEntity<>(ratingsService.getTaxiUserRating(requestDTO), HttpStatus.OK);
    }

    @PostMapping("/init")
    public ResponseEntity<RatingResponseDTO> initTaxiUser(@RequestBody @Valid TaxiUserRequestDTO requestDTO) {
        return new ResponseEntity<>(ratingsService.initTaxiUserRatings(requestDTO), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<RatingResponseDTO> rateTaxiUser(@RequestBody @Valid RatingDTO ratingDTO) {
        return new ResponseEntity<>(ratingsService.save(ratingDTO), HttpStatus.CREATED);
    }
}
