package io.styko.common.service;

import org.openqa.selenium.WebDriver;

public interface WebDriverProvider {

  WebDriver createWebDriver();
}
