package io.styko.nehnutelnosti;

import io.styko.common.persistance.Ad;
import io.styko.common.service.Scrapeable;
import java.util.Optional;
import java.util.regex.Pattern;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
public class NehnutelnostiScraper implements Scrapeable {

  private static final Pattern PATTERN = Pattern.compile("\"(https://www.nehnutelnosti.sk/\\d*/.*?)\"");

  @Override
  public Pattern getPattern() {
    return PATTERN;
  }

  @Override
  public Optional<Ad> scrapeAd(WebDriver webDriver, String link) {
    return Optional.empty(); //TODO
  }
}
