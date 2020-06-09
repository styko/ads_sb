package io.styko.common.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class RegexStringConverter {

  public String convert(String text, Pattern pattern) {
    Matcher matcher = pattern.matcher(text);
    return matcher.find() ? matcher.group(1) : "";
  }
}
