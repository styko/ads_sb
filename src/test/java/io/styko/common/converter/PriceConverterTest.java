package io.styko.common.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.google.gson.Gson;
import io.styko.common.config.GsonConfig;
import io.styko.common.persistance.Price;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriceConverterTest {

  public static final String PRICES_CONVERTED_TO_STRING = "[{\"updated\":\"2020-03-10T23:00:00Z\",\"value\":1},{\"updated\":\"2020-03-10T23:00:00Z\",\"value\":1}]";
  public static final List<Price> PRICES = Arrays.asList(
      Price.builder().updated(Instant.parse("2020-03-10T23:00:00Z")).value(BigDecimal.ONE).build(),
      Price.builder().updated(Instant.parse("2020-03-10T23:00:00Z")).value(BigDecimal.ONE).build()
  );
  private final Gson gson = new GsonConfig().gson();

  private PriceConverter priceConverter = new PriceConverter(gson);

  @Test
  void convertToDatabaseColumn() {
    String converted = priceConverter.convertToDatabaseColumn(PRICES);
    assertThat(converted).isEqualTo(PRICES_CONVERTED_TO_STRING);
  }

  @Test
  void convertToEntityAttribute() {
    List<Price> convertedPrices = priceConverter.convertToEntityAttribute(PRICES_CONVERTED_TO_STRING);
    assertThat(convertedPrices).isEqualTo(PRICES);
  }
}
