package by.modsen.taxiprovider.ratingservice.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/rating.feature",
        glue = "by.modsen.taxiprovider.ratingservice.component",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RatingServiceComponentTest {
}
