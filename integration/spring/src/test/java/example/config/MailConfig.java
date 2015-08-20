package example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan("example.config")
@ImportResource("/META-INF/spring/testContext.xml")
public class MailConfig {

}
