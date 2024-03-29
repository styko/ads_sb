package io.styko.common.scheduler;

import io.styko.common.gmail.GmailScrapper;
import io.styko.common.persistance.AdRepository;
import io.styko.common.service.WebScraperService;
import io.styko.security.service.ContextHack;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {
  private static final long SECONDS_30 = 30 * 1000L;

  @Autowired
  private GmailScrapper gmailScrapper;

  @Autowired
  private WebScraperService webScraperService;

  @Autowired
  private AdRepository adRepository;

  @Scheduled( fixedDelay = SECONDS_30)
  public void scrapeAdsFromEmail() throws IOException {
    ContextHack.dataRestHackForAuth();
    List<String> linksFromMessages = gmailScrapper.scrapeLinksFromMessages();
    webScraperService.scrapeAds(linksFromMessages);
    SecurityContextHolder.clearContext();
  }

  @Scheduled( cron = "${io.styko.findInactiveAdsJob.cron}" )
  public void findInactiveAdsJob(){
    ContextHack.dataRestHackForAuth();
    log.info("findInactiveAdsJob has been scheduled");
    List<String> activeLinks = adRepository.findAllLinksByDeactivatedIsNull();
    log.info("count of active links {}", activeLinks.size());
    webScraperService.scrapeAds(activeLinks);
    SecurityContextHolder.clearContext();
  }
}
