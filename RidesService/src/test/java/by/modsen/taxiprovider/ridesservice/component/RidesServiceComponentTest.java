package by.modsen.taxiprovider.ridesservice.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/ride.feature",
        glue = "by.modsen.taxiprovider.ridesservice.component",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RidesServiceComponentTest {
}
