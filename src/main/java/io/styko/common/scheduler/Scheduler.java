package io.styko.common.scheduler;

import io.styko.common.gmail.GmailScrapper;
import io.styko.common.service.WebScraperService;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
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

  @Scheduled( fixedDelay= SECONDS_30)
  public void schedule() throws IOException {
    List<String> linksFromMessages = gmailScrapper.scrapeLinksFromMessages();
    webScraperService.scrapeAds(linksFromMessages);
  }
}
