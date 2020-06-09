package io.styko.common.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile({"!remote"})
public class LocalWebDriverService implements WebDriverProvider {

  @Autowired
  private WebDriverService webDriverService;

  @Value("${io.styko.driverPath}")
  private String driverPath;

  public WebDriver createWebDriver() {
    log.info("create local web driver");
    System.setProperty("webdriver.gecko.driver", driverPath);

    return webDriverService.setupDriver(
        new FirefoxDriver(webDriverService.getFirefoxOptions())
    );
  }
}
