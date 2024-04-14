package by.modsen.taxiprovider.endtoendtest.config;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/e2e.feature",
        glue = "by.modsen.taxiprovider.endtoendtest.component",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class EndToEndTests {
}
