package com.webage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizationServerDaoImpl implements AuthorizationServerDao {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServerDaoImpl.class);

    @Value("${authorization.server.uri}")	String tokenUri;
    @Value("${authorization.server.port}")	String port;
    @Value("${authorization.server.user}")	String user;
    @Value("${authorization.server.password}")	String password;

    @Autowired
    RestClient client;

	private String serverUri() {
		return String.format(tokenUri,port);
	}


    public String getJwt() {
        ResponseEntity<String> responseEntity = client
                .post()
                .uri(serverUri())
                .headers( headers -> headers.setBasicAuth(user,password) )
                .retrieve()
                .toEntity(String.class);

        if( !responseEntity.getStatusCode().is2xxSuccessful() ) {
            String msg =
                    String.format(
                            "Error retrieving token from authorization server at %s, return code %s, body %s",
                            tokenUri,
                            responseEntity.getStatusCode(),
                            responseEntity.getBody() );
            LOG.atError().log(msg);
            throw new RuntimeException(msg);
        }
        String jwt = responseEntity.getBody();
        LOG.atInfo().log(String.format("JSON Web Token returned from auth server is %s",jwt));
        return jwt;
    }

}
