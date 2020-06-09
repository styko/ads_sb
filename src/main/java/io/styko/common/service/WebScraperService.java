package io.styko.common.service;

import io.styko.common.persistance.Ad;
import io.styko.common.persistance.AdRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebScraperService {

  @Autowired
  private WebDriverProvider webDriverProvider;

  @Autowired
  private List<Scrapeable> scrapeables;

  @Autowired
  private AdRepository adRepository;

  public void scrapeAds(List<String> linksFromMessages) {
    if(linksFromMessages.isEmpty()){
      return;
    }

    WebDriverPool webDriverPool = new WebDriverPool(webDriverProvider, linksFromMessages.size());

    try {
      linksFromMessages.parallelStream().forEach(link -> {
        dataRestHackForAuth();
        for (Scrapeable scraper : scrapeables) {
          Optional<Ad> ad = scrapeAd(webDriverPool, link, scraper);
          if(ad.isPresent()){
            adRepository.save(ad.get());
            log.info("Saved ad {}", ad.get());
          }
        }
        SecurityContextHolder.clearContext();
      });
    } finally {
      webDriverPool.quit();
    }
  }

  /**
   * This is needed for AdRepository to work with data rest and spring security
   */
  private void dataRestHackForAuth() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("system",
            "system", AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
  }


  private Optional<Ad> scrapeAd(WebDriverPool webDriverPool, String link, Scrapeable scraper) {
    WebDriver webDriver = null;
    for (int retries = 1; retries <= 5; retries++) {
      try {
        webDriver = webDriverPool.borrow();
        return scraper.scrapeAd(webDriver, link);
      } catch (WebDriverException e){
        log.warn("WebDriverException has been catched {}, retries counter {}, link {}", e.getMessage(), retries, link);
      } finally {
        webDriverPool.giveBack(webDriver);
      }
    }
    log.error("Ad was not scraped as all retries for scraping were used, link={}", link);
    return Optional.empty();
  }
}
