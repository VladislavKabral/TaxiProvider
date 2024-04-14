package by.modsen.taxiprovider.passengerservice.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/passenger.feature",
        glue = "by.modsen.taxiprovider.passengerservice.component",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class PassengerServiceComponentTest {
}
