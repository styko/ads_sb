package io.styko.common.converter;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StringToInstantConverter {

  private Clock clock;

  @Autowired
  public StringToInstantConverter(Clock clock) {
    this.clock = clock;
  }

  private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("d.M.yyyy' 'HH:mm:ss")
      .appendFraction(ChronoField.NANO_OF_SECOND, 0, 5, true).toFormatter();

  public Instant convert(String input){
    ZoneOffset offset = clock.instant().atZone(ZoneId.of("Europe/Bratislava")).getOffset();
    return LocalDateTime.parse(input, formatter).atOffset(offset).toInstant();
  }
}
