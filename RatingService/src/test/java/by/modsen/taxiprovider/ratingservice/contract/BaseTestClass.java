package by.modsen.taxiprovider.ratingservice.contract;

import by.modsen.taxiprovider.ratingservice.RatingServiceApplication;
import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.service.RatingsService;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import static by.modsen.taxiprovider.ratingservice.utility.RatingsTestUtil.*;
import static by.modsen.taxiprovider.ratingservice.util.Message.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RatingServiceApplication.class)
public class BaseTestClass {

    @MockBean
    private RatingsService ratingsService;

    @Autowired
    protected WebApplicationContext context;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        when(ratingsService.getTaxiUserRating(getRequestForDriverRating()))
                .thenReturn(getDriversRating());

        when(ratingsService.getTaxiUserRating(getRequestForPassengerRating()))
                .thenReturn(getPassengerRating());

        when(ratingsService.initTaxiUserRatings(getRequestForDriverRating()))
                .thenReturn(getDriverRatingResponse());

        when(ratingsService.initTaxiUserRatings(getRequestForPassengerRating()))
                .thenReturn(getPassengerRatingResponse());

        when(ratingsService.save(getRequestForRateDriver()))
                .thenReturn(getNotDefaultDriverRatingResponse());

        when(ratingsService.save(getRequestForRatePassenger()))
                .thenReturn(getNotDefaultPassengerRatingResponse());

        RatingDto invalidRequest = getInvalidRequestWithInvalidRoleForRateDriver();
        when(ratingsService.save(invalidRequest))
                .thenThrow(new EntityValidateException(String.format(ROLE_IS_INVALID, invalidRequest.getRole())));

        RestAssuredMockMvc.webAppContextSetup(this.context);
    }
}
