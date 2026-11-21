package io.styko.common.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile({"!remote && !prod"})
public class LocalWebDriverService implements WebDriverProvider {

  @Autowired
  private WebDriverService webDriverService;

  public WebDriver createWebDriver() {
    log.info("create local web driver");

    WebDriver webDriver = webDriverService.setupDriver(
        new ChromeDriver(webDriverService.getChromeOptions()));
    return webDriverService.setupDriver(webDriver);
  }
}
