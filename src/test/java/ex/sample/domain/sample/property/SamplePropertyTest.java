package ex.sample.domain.sample.property;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class SamplePropertyTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(TestConfig.class)
        .withPropertyValues(
            "sample.api.url=http://localhost:8080/samples/properties",
            "sample.api.key=SAMPLE_API_KEY"
        );

    @Test
    void sampleProperty_test() {
        contextRunner.run(context -> {
            SampleProperty property = context.getBean(SampleProperty.class);

            assertThat(property.url()).isEqualTo("http://localhost:8080/samples/properties");
            assertThat(property.key()).isEqualTo("SAMPLE_API_KEY");
            assertThat(property.getUrlWithKey()).isEqualTo("http://localhost:8080/samples/properties?key=SAMPLE_API_KEY");
        });
    }

    @EnableConfigurationProperties(SampleProperty.class)
    static class TestConfig {

    }
}
