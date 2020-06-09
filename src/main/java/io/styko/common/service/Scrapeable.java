package io.styko.common.service;

import io.styko.common.persistance.Ad;
import java.util.Optional;
import java.util.regex.Pattern;
import org.openqa.selenium.WebDriver;

public interface Scrapeable {

  Pattern getPattern();

  Optional<Ad> scrapeAd(WebDriver webDriver, String link);
}
