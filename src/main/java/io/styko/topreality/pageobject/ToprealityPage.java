package io.styko.topreality.pageobject;

import io.styko.common.pageobject.PageObject;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class ToprealityPage implements PageObject {

  private WebDriver driver;

  @FindBy(css = "body > div.gray-bg.tr-border-top.tr-border-bottom > div > div > div > div.row.mt-3 > div.col-lg-8.gallery-info.pr-lg-0 > div.row > div.col-md-5.col-sm-12.properties.pl-md-0 > ul")
  private WebElement propertiesList;

  @FindBy(css = "body > div.gray-bg.tr-border-top.tr-border-bottom > div > div > div > div.d-flex > div.mr-auto > h1")
  private WebElement title;

  @FindBy(css ="body > div.gray-bg.tr-border-top.tr-border-bottom > div > div > div > div.row.mt-3 > div.col-lg-8.gallery-info.pr-lg-0 > div.row > div.col-md-5.col-sm-12.properties.pl-md-0 > ul > li.list-group-item.d-flex.justify-content-between.align-items-center.priceContainer.odd > div > meta:nth-child(3)")
  private WebElement price;

  @FindBy(css ="body > div.gray-bg.tr-border-top.tr-border-bottom > div > div > div > div.row.mt-3 > div.col-lg-8.gallery-info.pr-lg-0 > div.description > p")
  private WebElement description;

  @FindBy(css = "#map_canvas")
  private WebElement map;

  @FindAll({
      @FindBy(css="#fixedContact > div.contactBox.pb-2.pt-2.d-flex.flex-wrap > div.showContact.d-grid.col-8.p-0 > span > span.badge.btn.btn-danger.showMsg"),
      @FindBy(css="#fixedContact > div.contactBox.pb-2.pt-2.d-flex.flex-wrap > div.showContact.col-8.p-0 > span > span.badge.btn.btn-danger.showMsg"),
  })
  private WebElement showContactButton;

  @FindBy(css ="#fixedContact > div.contactBox.pb-2.pt-2.d-flex.flex-wrap > div.col-8.p-0 > span > a")
  private WebElement contact;

  @FindBy(css ="#fixedContact > div > div")
  private WebElement alternativeContact;

  @FindBy(css =".firstImage")
  private WebElement primaryImage;

  @FindBy(css = "#showMore")
  private WebElement showMore;

  public ToprealityPage(WebDriver driver, String link) {
    this.driver = driver;
    this.driver.get(link);
    PageFactory.initElements(driver, this);
  }

  public boolean isPageFound(){
    return this.driver.getPageSource().contains("Kontakt na predajcu");
  }

  public boolean isContactButtonPresent() {
    return this.driver.getPageSource().contains("<span class=\"badge btn btn-danger showMsg\">kliknite TU !</span>");
  }

  public boolean isShowMorePresent() {
    return this.driver.getPageSource().contains("<button id=\"showMore\" class=\"btn btn-outline-danger description-button mt-2\">Zobraziť celý popis</button>");
  }

  public boolean isMapPresent() {
    return this.driver.getPageSource().contains("#map_canvas");
  }

}
