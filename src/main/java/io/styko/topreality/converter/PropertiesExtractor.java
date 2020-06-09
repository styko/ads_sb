package io.styko.topreality.converter;

import io.styko.common.converter.NumberConverter;
import io.styko.common.converter.RegexStringConverter;
import io.styko.common.converter.StringToInstantConverter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PropertiesExtractor {

  private static final Pattern TAKE_FIRST_PRICE_PATTERN = Pattern.compile("(.*mesiac)");
  private static final Pattern CURRENCY_PATTERN = Pattern.compile("(€|Kč|CZK|EUR)");
  private static final Pattern PRICE_PATTERN = Pattern.compile("CENA\\n(.*)");
  private static final Pattern LOCALITY_PATTERN = Pattern.compile("LOKALITA\\n(.*)");
  private static final Pattern ADDRESS_PATTERN = Pattern.compile("ULICA\\n(.*)");
  private static final Pattern SIZE_PATTERN = Pattern.compile("ÚŽITKOVÁ PLOCHA\\n(.*)m2");
  private static final Pattern CATEGORY_PATTERN = Pattern.compile("KATEGÓRIA\\n(.*)");
  private static final Pattern UPDATED_PATTERN = Pattern.compile("AKTUALIZÁCIA\\n(.*)");

  private NumberConverter numberConverter;
  private RegexStringConverter regexStringConverter;
  private StringToInstantConverter stringToInstantConverter;

  @Autowired
  public PropertiesExtractor(
      NumberConverter numberConverter,
      RegexStringConverter regexStringConverter,
      StringToInstantConverter stringToInstantConverter) {
    this.numberConverter = numberConverter;
    this.regexStringConverter = regexStringConverter;
    this.stringToInstantConverter = stringToInstantConverter;
  }

  public String parseLocality(String propertiesText) {
    return regexStringConverter.convert(propertiesText, LOCALITY_PATTERN).trim();
  }

  public String parseAddress(String propertiesText) {
    return regexStringConverter.convert(propertiesText, ADDRESS_PATTERN).trim();
  }

  public String parseCategory(String propertiesText) {
    return regexStringConverter.convert(propertiesText, CATEGORY_PATTERN).trim();
  }

  public Integer parseSize(String propertiesText) {
    return numberConverter.convert(regexStringConverter.convert(propertiesText, SIZE_PATTERN));
  }
  // TODO limit outcome to enum
  public String parseCurrency(String propertiesText) {
    String priceValueAsText = regexStringConverter.convert(propertiesText, PRICE_PATTERN);
    return regexStringConverter.convert(priceValueAsText, CURRENCY_PATTERN);
  }

  public BigDecimal parsePrice(String propertiesText) {
    String priceValueAsText = regexStringConverter.convert(propertiesText, PRICE_PATTERN);
    log.debug("priceValueAsText {}", priceValueAsText);
    String takeFirstPrice = regexStringConverter.convert(priceValueAsText, TAKE_FIRST_PRICE_PATTERN);
    log.debug("takeFirstPrice {}", takeFirstPrice);
    return numberConverter.convertToBigDecimal(takeFirstPrice.isEmpty() ? priceValueAsText : takeFirstPrice);
  }

  public Instant parseUpdated(String propertiesText) {
    return stringToInstantConverter.convert(regexStringConverter.convert(propertiesText, UPDATED_PATTERN));
  }
}
