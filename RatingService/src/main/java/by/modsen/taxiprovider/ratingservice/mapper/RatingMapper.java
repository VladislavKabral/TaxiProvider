package by.modsen.taxiprovider.ratingservice.mapper;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.model.Rating;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper {

    private final ModelMapper modelMapper;

    public Rating toEntity(RatingDto ratingDTO) {
        return modelMapper.map(ratingDTO, Rating.class);
    }
}
