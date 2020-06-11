package io.styko.common.service;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebDriverService {

  @Value("${io.styko.headless}")
  private Boolean headless;

  FirefoxOptions getFirefoxOptions() {
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setHeadless(headless);
    return firefoxOptions;
  }

  WebDriver setupDriver(WebDriver driver) {
    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    return driver;
  }
}
