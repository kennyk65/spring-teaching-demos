package com.example.demo;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenLoggingService implements OAuth2AuthorizedClientService {

    private final OAuth2AuthorizedClientService delegate;
    private static final Logger logger = LoggerFactory.getLogger(TokenLoggingService.class);

    public TokenLoggingService(OAuth2AuthorizedClientService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, org.springframework.security.core.Authentication principal) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        if (accessToken != null) {
            logger.info("Access Token: {}", accessToken.getTokenValue());
        }
        delegate.saveAuthorizedClient(authorizedClient, principal);
    }

    @Override
    public OAuth2AuthorizedClient loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return delegate.loadAuthorizedClient(clientRegistrationId, principalName);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        delegate.removeAuthorizedClient(clientRegistrationId, principalName);
    }
}
