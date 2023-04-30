package jp.co.axa.apidemo.repository.impl.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

  @Value("${spring.data.database.uri}")
  private String DATABASE_URI;
  @Value("${database.driver.class.name}")
  private String DATABASE_DRIVER_CLASS_NAME;
  @Value("${spring.data.database.user}")
  private String DATABASE_USER;
  @Value("${spring.data.database.password}")
  private String DATABASE_PASSWORD;

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName(DATABASE_DRIVER_CLASS_NAME);
    dataSourceBuilder.url(DATABASE_URI);
    dataSourceBuilder.username(DATABASE_USER);
    dataSourceBuilder.password(DATABASE_PASSWORD);
    return dataSourceBuilder.build();
  }


}
