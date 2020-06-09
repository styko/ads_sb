package io.styko.topreality.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import io.styko.common.converter.NumberConverter;
import io.styko.common.converter.RegexStringConverter;
import io.styko.common.converter.StringToInstantConverter;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertiesExtractorTest {
private static String TESTING_PROP_1 = "CENA\n"
    + "    1 400 000 €\n"
    + "2 222 €/m2\n"
    + "      HYPOTÉKA\n"
    + "  od 4433 EUR/mesiac\n"
    + "  Chcem porovnanie\n"
    + "  LOKALITA\n"
    + "  Bratislava I, Staré Mesto\n"
    + "      ULICA\n"
    + "  Hrebendova\n"
    + "      AKTUALIZÁCIA\n"
    + "19.5.2020 09:43:19\n"
    + "  IDENTIFIKAČNÉ ČÍSLO:\n"
    + "      447089\n"
    + "  ÚŽITKOVÁ PLOCHA\n"
    + "630 m2\n"
    + "      POZEMOK\n"
    + "1000 m2\n"
    + "      KATEGÓRIA\n"
    + "  Rodinný dom / predaj\n"
    + "  www.topreality.sk/id7242395";
  private static String TESTING_PROP_2 = "     CENA\n"
      + "14 300 CZK/mesiac\n"
      + "250,88 CZK/mesiac/m2\n"
      + "      LOKALITA\n"
      + "  Kolín, časť Kolín II\n"
      + "      ULICA\n"
      + "  Masarykova\n"
      + "      AKTUALIZÁCIA\n"
      + "2.5.2020 16:37:05\n"
      + "  IDENTIFIKAČNÉ ČÍSLO:\n"
      + "      28183\n"
      + "  ÚŽITKOVÁ PLOCHA\n"
      + "57 m2\n"
      + "      POSCHODIE\n"
      + "7 / 8\n"
      + "  BALKÓN / LOGGIA\n"
      + "      Áno\n"
      + "  PIVNICA\n"
      + "      Áno\n"
      + "  MATERIÁL\n"
      + "      panel\n"
      + "  STAV\n"
      + "      pôvodný\n"
      + "  KATEGÓRIA\n"
      + "  Mezonet / prenájom\n"
      + "  www.topreality.sk/id7225618";


  private NumberConverter numberConverter = new NumberConverter();

  private RegexStringConverter regexStringConverter = new RegexStringConverter();

  @Mock
  private Clock clock;

  @InjectMocks
  private StringToInstantConverter stringToInstantConverter;

  private PropertiesExtractor propertiesExtractor;

  @BeforeEach
  void setUp() {
    propertiesExtractor = new PropertiesExtractor(numberConverter, regexStringConverter, stringToInstantConverter);
  }

  @Test
  void parseLocality() {
    assertThat(propertiesExtractor.parseLocality(TESTING_PROP_1)).isEqualTo("Bratislava I, Staré Mesto");
    assertThat(propertiesExtractor.parseLocality(TESTING_PROP_2)).isEqualTo("Kolín, časť Kolín II");
  }

  @Test
  void parseAddress() {
    assertThat(propertiesExtractor.parseAddress(TESTING_PROP_1)).isEqualTo("Hrebendova");
    assertThat(propertiesExtractor.parseAddress(TESTING_PROP_2)).isEqualTo("Masarykova");
  }

  @Test
  void parseCategory() {
    assertThat(propertiesExtractor.parseCategory(TESTING_PROP_1)).isEqualTo("Rodinný dom / predaj");
    assertThat(propertiesExtractor.parseCategory(TESTING_PROP_2)).isEqualTo("Mezonet / prenájom");
  }

  @Test
  void parseSize() {
    assertThat(propertiesExtractor.parseSize(TESTING_PROP_1)).isEqualTo(630);
    assertThat(propertiesExtractor.parseSize(TESTING_PROP_2)).isEqualTo(57);
  }

  @Test
  void parseCurrency() {
    assertThat(propertiesExtractor.parseCurrency(TESTING_PROP_1)).isEqualTo("€");
    assertThat(propertiesExtractor.parseCurrency(TESTING_PROP_2)).isEqualTo("CZK");
  }

  @Test
  void parsePrice() {
    assertThat(propertiesExtractor.parsePrice(TESTING_PROP_1)).isEqualTo(BigDecimal.valueOf(1400000));
    assertThat(propertiesExtractor.parsePrice(TESTING_PROP_2)).isEqualTo(BigDecimal.valueOf(14300));
  }

  @Test
  void parseUpdated() {
    lenient().when(clock.instant()).thenReturn(Instant.parse("2020-07-24T13:00:00Z"));
    assertThat(propertiesExtractor.parseUpdated(TESTING_PROP_1)).isEqualTo(Instant.parse("2020-05-19T07:43:19Z"));
    assertThat(propertiesExtractor.parseUpdated(TESTING_PROP_2)).isEqualTo(Instant.parse("2020-05-02T14:37:05Z"));
  }

}
