package io.styko.common.gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GmailService {
  @Autowired
  private JsonFactory jsonFactory;

  @Autowired
  private NetHttpTransport netHttpTransport;

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);

  @Value("${io.styko.applicationName}")
  private String applicationName;

  @Value("${io.styko.tokensPath}")
  private String tokensPath;

  @Value("${io.styko.credentials}")
  private String credentials;

  public Gmail getGmail() throws IOException {
    return new Gmail.Builder(netHttpTransport, jsonFactory, getCredentials())
        .setApplicationName(applicationName)
        .build();
  }

  private Credential getCredentials() throws IOException {
    InputStream credentialsStream = new ByteArrayInputStream(credentials.getBytes(StandardCharsets.UTF_8));
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
        jsonFactory,
        new InputStreamReader(credentialsStream)
    );

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        netHttpTransport, jsonFactory, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new File(tokensPath)))
        .setAccessType("offline")
        .build();

    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    return new AuthorizationCodeInstalledApp(flow, receiver).authorize(applicationName);
  }
}
