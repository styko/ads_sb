package io.styko.common.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {
  public static final ZoneId ZONEID_BRATISLAVA = ZoneId.of("Europe/Bratislava");

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
