package by.modsen.taxiprovider.driverservice.mapper.rating;

import by.modsen.taxiprovider.driverservice.dto.rating.DriverRatingDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.driverservice.model.rating.DriverRating;
import by.modsen.taxiprovider.driverservice.model.rating.Rating;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper {

    private final ModelMapper modelMapper;

    public Rating toEntity(RatingDTO ratingDTO) {
        return modelMapper.map(ratingDTO, Rating.class);
    }

    public RatingDTO toDTO(Rating rating) {
        return modelMapper.map(rating, RatingDTO.class);
    }

    public DriverRatingDTO toDTO(DriverRating driverRating) {
        return modelMapper.map(driverRating, DriverRatingDTO.class);
    }
}
