package io.styko.common.converter;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class NumberConverter {

  public Integer convert(String input) {
    String text = input.replaceAll("[^\\d]", "");
    return !text.isEmpty() ? Integer.valueOf(text) : null;
  }

  public BigDecimal convertToBigDecimal(String input) {
    String text = input.replaceAll("[^\\d,]", "").replace(",",".");
    return !text.isEmpty() ? new BigDecimal(text) : null;
  }
}
