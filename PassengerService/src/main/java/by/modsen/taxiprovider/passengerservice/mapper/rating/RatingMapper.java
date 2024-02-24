package by.modsen.taxiprovider.passengerservice.mapper.rating;

import by.modsen.taxiprovider.passengerservice.dto.rating.PassengerRatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.model.rating.PassengerRating;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public RatingMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Rating toEntity(RatingDTO ratingDTO) {
        return modelMapper.map(ratingDTO, Rating.class);
    }

    public PassengerRatingDTO toDTO(PassengerRating passengerRating) {
        return modelMapper.map(passengerRating, PassengerRatingDTO.class);
    }
}
