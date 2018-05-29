This project attempts to implement a simple OAuth2 Authorization Server.
It borrows (steals) heavily from Dave Syer's OAuth2 Server example provided
as part of the Spring Cloud demos:  https://github.com/spring-cloud-samples/authserver

Firstly this is a standard Spring Boot web app.  The contextPath and port are altered
to allow you to run it on localhost alongside clients without port conflicts or cookie
problems, so access it at http://localhost:8050/authserver .  You can log into it using credentials I've hard-coded into the login page. 

The server is hard-coded with a single client and secret.
Client ID: theOneAndOnlyClient
Client Secret:  DoNotTellAnyone
As far as I know, there is no (easy) way to support multiple clients.

* `/authserver/oauth/token` the Token endpoint, for clients to acquire access
  tokens. There is one client ("acme" with secret "acmesecret"). With
  Spring Cloud Security this is the `oauth2.client.tokenUri`.
* `/authserver/oauth/authorize` the Authorization endpoint to obtain user
  approval for a token grant.  Spring Cloud Security configures this
  in a client app as `oauth2.client.authorizationUri`.
* `/authserver/oauth/check_token` the Check Token endpoint (not part of the
  OAuth2 spec). Can be used to decode a token remotely. Spring Cloud
  Security configures this in a client app as
  `oauth2.resource.tokenInfoUri`.
