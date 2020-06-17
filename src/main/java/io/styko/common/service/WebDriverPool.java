package io.styko.common.service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class WebDriverPool {

  private final BlockingQueue<WebDriver> objects;
  private final int maxPoolSize;

  public WebDriverPool(WebDriverProvider webDriverProvider, int linksCount, int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
    List<WebDriver> webDrivers = IntStream.rangeClosed(1, getSizeOfPool(linksCount))
        .mapToObj(i -> webDriverProvider.createWebDriver())
        .collect(Collectors.toList());
    this.objects = new ArrayBlockingQueue<>(webDrivers.size(), true, webDrivers);
  }

  private int getSizeOfPool(int linksCount) {
    int linksDivided = linksCount / maxPoolSize;
    int spawnThreads = linksDivided == 0 ? 1 : Math.min(linksDivided, maxPoolSize);
    log.info("spawnThreads {}", spawnThreads);
    return spawnThreads;
  }

  public WebDriver borrow() {
    try {
      return this.objects.take();
    } catch (InterruptedException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void giveBack(WebDriver webDriver) {
    try {
      this.objects.put(webDriver);
    } catch (InterruptedException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void quit() {
    this.objects.forEach(WebDriver::quit);
  }

}
