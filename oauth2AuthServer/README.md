This project attempts to implement a simple OAuth2 Authorization Server.
It borrows (steals) heavily from Dave Syer's OAuth2 Server example provided
as part of the Spring Cloud demos:  https://github.com/spring-cloud-samples/authserver

Firstly this is a standard Spring Boot web app.  The contextPath and port are altered
to allow you to run it on localhost alongside clients without port conflicts or cookie
problems, so access it at `http://localhost:8050/authserver` .  You can log into it using credentials I've hard-coded into the login page. 

The server is hard-coded with a single client and secret.
Client ID: theOneAndOnlyClient
Client Secret:  DoNotTellAnyone
As far as I know, there is no (easy) way to support multiple clients.

This server exposes a few endpoints that the client will need to connect up with,
depending on which part of the OAuth2 flow is happening:

* `/authserver/oauth/authorize` the Authorization endpoint to obtain user
  approval for a token grant.  On the client side this is set using
   `spring.security.oauth2.client.provider.<auth-server-name>.authorizationUri`.

* `/authserver/oauth/token` the Token endpoint, for clients to acquire access
  tokens given a code obtained earlier.  On the client side this is set with 
   `spring.security.oauth2.client.provider.<auth-server-name>.tokenUri`.

* `/authserver/oauth/????` Used to obtain "User Info".  It might be 'user' or 'userinfo',
	I can't tell.  The client needs to know what this URL is in the  `spring.security.oauth2.client.provider.<auth-server-name>..<auth-server-name>.userInfoUri`.  TODO - DETERMINE THIS VALUE.
	
WHERE I LEFT OFF:
1. Not sure what values to use for "scope".  values like "user" and "openid" seem to have 
specific meanings.
2. Not sure what value is to be used for 'UserInfo" uri.
3. Don't know how / if I should generate JWT token to avoid these hassles.
