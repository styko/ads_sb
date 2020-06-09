package io.styko;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RealEstateEmailAggregatorApplication {

  public static void main(String[] args) {
    setJvmDefaultTimeZone();
    SpringApplication.run(RealEstateEmailAggregatorApplication.class, args);
  }

  // used to ensure JVM JDBC/JPA and H2 starts in UTC
  private static void setJvmDefaultTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
}
