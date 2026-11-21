package io.styko.common.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebDriverService {

  @Value("${io.styko.headless}")
  private Boolean headless;

  ChromeOptions getChromeOptions() {
    Map<String, Object> prefs = new HashMap<>();
    prefs.put("profile.default_content_settings.popups", 0);

    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.setExperimentalOption("prefs", prefs);
    chromeOptions.setHeadless(headless);

    return chromeOptions;
  }

  WebDriver setupDriver(WebDriver driver) {
    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    return driver;
  }
}
