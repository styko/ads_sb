package io.styko.common.gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import com.google.common.collect.Lists;
import io.styko.common.service.Scrapeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GmailScrapper {

  public static final String UNREAD_LABEL = "UNREAD";

  @Autowired
  private GmailService gmailService;

  @Autowired
  private List<Scrapeable> scrapeables;

  public static final String USER_ID = "me";

  public List<String> scrapeLinksFromMessages() throws IOException {
    Gmail service = gmailService.getGmail();

    List<String> linksFromMessages = getMessages(service)
        .stream()
        .map(message -> getMessageWithContent(service, message))
        .map(messageWithContent -> {
          log.info("Email with subject '{}' will be processed", getSubject(messageWithContent));
          markMessageAsRead(service, messageWithContent);
          List<String> messageLinks = getLinksFrom(messageWithContent);
          messageLinks.forEach(log::info);
          return messageLinks;
        })
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
    log.info("Scraped {} links", linksFromMessages.size());
    return linksFromMessages;
  }

  private List<String> getLinksFrom(Message messageWithContent) {
    return getRelevantParts(messageWithContent)
        .stream()
        .map(part -> {
          String s = new String(part.getBody().decodeData());

          return scrapeables.stream()
              .map(Scrapeable::getPattern)
              .flatMap(pattern -> pattern.matcher(s).results())
              .map(matchResult -> matchResult.group(1))
              .collect(Collectors.toList());
        })
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }

  private Message getMessageWithContent(Gmail service, Message message) {
    try {
      return service.users().messages().get(USER_ID, message.getId()).execute();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private void markMessageAsRead(Gmail service, Message messageWithContent) {
    try {
      ModifyMessageRequest modifyMessageRequest = new ModifyMessageRequest();
      modifyMessageRequest.setRemoveLabelIds(Collections.singletonList(UNREAD_LABEL));
      service.users().messages().modify(USER_ID, messageWithContent.getId(), modifyMessageRequest)
          .execute();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private List<Message> getMessages(Gmail service) throws IOException {
    ListMessagesResponse response = service.users().messages().list(USER_ID).setQ("is:unread")
        .execute();

    log.debug("scraping gmail, estimated size of unread messages: {}",
        response.getResultSizeEstimate());

    return response.getResultSizeEstimate() > 0 ? Lists.reverse(response.getMessages())
        : new ArrayList<>();
  }

  private String getSubject(Message messageWithContent) {
    return messageWithContent.getPayload().getHeaders().stream()
        .filter(header -> "Subject".equals(header.getName()))
        .map(MessagePartHeader::getValue)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Could not get email subject"));
  }

  private List<MessagePart> getRelevantParts(Message message) {
    List<MessagePart> parts = message.getPayload().getParts();
    return parts != null ? parts.stream()
        .filter(part -> part.getMimeType().contains("text/html"))
        .collect(Collectors.toList()) : new ArrayList<>();
  }
}
