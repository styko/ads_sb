package io.styko.security.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextHack {

  private ContextHack() {
  }

  /**
   * This is needed for AdRepository to work with data rest and spring security
   */
  public static void dataRestHackForAuth() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("system",
            "system", AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
  }
}
