package io.styko.topreality.scraper;

import io.styko.common.persistance.Ad;
import io.styko.common.persistance.Price;
import io.styko.common.scraper.Scraper;
import io.styko.topreality.converter.PropertiesExtractor;
import io.styko.topreality.pageobject.ToprealityPage;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ToprealityScraper extends Scraper<ToprealityPage> {

  private static final String LINK_REGEX = "https://www.topreality.sk/id.*";
  private static final Pattern PATTERN = Pattern.compile("href=\"(" + LINK_REGEX + "?)\"");

  @Autowired
  private PropertiesExtractor propertiesExtractor;

  @Override
  public Pattern getPattern() {
    return PATTERN;
  }

  @Override
  protected String getLinkRegex() {
    return LINK_REGEX;
  }

  @Override
  protected ToprealityPage getPageObject(WebDriver webDriver, String link) {
    return new ToprealityPage(webDriver, link);
  }

  @Override
  protected Ad createAd(String link, ToprealityPage toprealityPage) {
    Ad ad;
    String contact;
    if(toprealityPage.isContactButtonPresent()){
      toprealityPage.getShowContactButton().click();
      contact = toprealityPage.getContact().getText();
    } else {
      contact = toprealityPage.getAlternativeContact().getText();
    }

    if(toprealityPage.isShowMorePresent()){
      toprealityPage.getShowMore().click();
    }

    String propertiesText = toprealityPage.getPropertiesList().getText();

    BigDecimal price = propertiesExtractor.parsePrice(propertiesText);
    Instant updated = propertiesExtractor.parseUpdated(propertiesText);

    ad = Ad.builder()
        .link(link)
        .title(toprealityPage.getTitle().getText())
        .locality(propertiesExtractor.parseLocality(propertiesText))
        .address(propertiesExtractor.parseAddress(propertiesText))
        .category(propertiesExtractor.parseCategory(propertiesText))
        .contact(contact)
        .countOfUpdates(0)
        .created(updated)
        .description(toprealityPage.getDescription().getText())
        .lastPrice(price)
        .prices(Collections.singletonList(Price.builder().value(price).updated(updated).build()))
        .currency(propertiesExtractor.parseCurrency(propertiesText))
        .size(propertiesExtractor.parseSize(propertiesText))
        .galleryUrl(toprealityPage.getPrimaryImage().getAttribute("href"))
        .build();

    if (toprealityPage.isMapPresent()){
      WebElement map = toprealityPage.getMap();
      ad.setLatitude(Double.valueOf(map.getAttribute("data-gpsx")));
      ad.setLongitude(Double.valueOf(map.getAttribute("data-gpsy")));
    }

    log.info("Ad is created {}", ad);
    return ad;
  }

  @Override
  protected void updateAd(ToprealityPage toprealityPage, Ad ad) {
    String propertiesText = toprealityPage.getPropertiesList().getText();
    BigDecimal price = propertiesExtractor.parsePrice(propertiesText);
    Instant updated = propertiesExtractor.parseUpdated(propertiesText);

    if (ad == null){
      log.info("Ad is null");
    }

    if (!ad.getLastPrice().equals(price) || !ad.getUpdated().equals(updated)) {
      ad.setLastPrice(price);
      ad.getPrices().add(Price.builder().value(price).updated(updated).build());
      ad.setCountOfUpdates(ad.getCountOfUpdates() + 1);
      ad.setDescription(toprealityPage.getDescription().getText());
      ad.setUpdated(updated);
      log.info("Ad is updated {}", ad);
    }
    log.info("Ad is the same so its not updated {}", ad);
  }
}
