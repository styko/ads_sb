package io.styko.common.scraper;

import io.styko.common.pageobject.PageObject;
import io.styko.common.persistance.Ad;
import io.styko.common.persistance.AdRepository;
import io.styko.common.service.Scrapeable;
import java.time.Instant;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class Scraper<T extends PageObject> implements Scrapeable {

  @Autowired
  private AdRepository adRepository;

  @Override
  public Optional<Ad> scrapeAd(WebDriver webDriver, String link) {
    if (!link.matches(getLinkRegex())){
      return Optional.empty();
    }

    log.info("parsing link {}", link);

    Optional<Ad> adFromDb = adRepository.findByLink(link);

    T pageObject = getPageObject(webDriver, link);
    boolean pageFound = pageObject.isPageFound();

    Ad ad;
    if(adFromDb.isPresent()){
      ad = adFromDb.get();

      if(pageFound){
        updateAd(pageObject, ad);
      } else {
        deactivateAd(ad);
      }

    } else if (pageFound && adFromDb.isEmpty()){
      ad = createAd(link, pageObject);
    } else {
      log.info("Ad wont be parsed as it is a new one and it doesnt exist anymore");
      return Optional.empty();
    }

    return Optional.of(ad);
  }

  private void deactivateAd(Ad ad) {
    ad.setDeactivated(Instant.now());

    log.info("Ad is deactivated {}", ad);
  }

  protected abstract String getLinkRegex();

  protected abstract T getPageObject(WebDriver webDriver, String link);

  protected abstract Ad createAd(String link, T page);

  protected abstract void updateAd(T page, Ad ad);
}
