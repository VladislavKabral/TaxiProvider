package by.modsen.taxiprovider.driverservice.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/driver.feature",
        glue = "by.modsen.taxiprovider.driverservice.component",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class DriverServiceComponentTest {
}
