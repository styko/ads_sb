package io.styko.common.service;

import java.net.MalformedURLException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"remote"})
@Slf4j
public class RemoteWebDriverService implements WebDriverProvider{

  @Autowired
  private WebDriverService webDriverService;

  @Value("${selenium.hub}")
  private String seleniumHub;

  public WebDriver createWebDriver() {
    log.info("create remote web driver");
    RemoteWebDriver driver = null;
    try {
      driver = new RemoteWebDriver(new URL(seleniumHub), webDriverService.getFirefoxOptions());
    } catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    }
    return webDriverService.setupDriver(driver);
  }
}
