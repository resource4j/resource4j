package example.config;

import com.github.resource4j.spring.test.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("example.config")
@Import(TestConfiguration.class)
public class MailConfig {

}
