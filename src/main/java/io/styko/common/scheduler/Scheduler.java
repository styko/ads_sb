package io.styko.common.scheduler;

import io.styko.common.persistance.AdRepository;
import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.styko.common.gmail.GmailScrapper;
import io.styko.common.service.WebScraperService;

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
    dataRestHackForAuth();
    List<String> linksFromMessages = gmailScrapper.scrapeLinksFromMessages();
    webScraperService.scrapeAds(linksFromMessages);
    SecurityContextHolder.clearContext();
  }

  @Scheduled( cron = "${io.styko.findInactiveAdsJob.cron}" )
  public void findInactiveAdsJob(){
    dataRestHackForAuth();
    log.info("Find findInactiveAdsJob has been scheduled");
    List<String> activeLinks = adRepository.findAllLinksByDeactivatedIsNull();
    log.info("Count of active links {}", activeLinks.size());
    webScraperService.scrapeAds(activeLinks);
    SecurityContextHolder.clearContext();
  }

  /**
   * This is needed for AdRepository to work with data rest and spring security
   */
  private void dataRestHackForAuth() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("system",
            "system", AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
  }

}
