package jp.co.axa.apidemo.config;

import jp.co.axa.apidemo.repository.impl.config.DatabaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DatabaseConfiguration.class)
public class AppConfiguration {
}
