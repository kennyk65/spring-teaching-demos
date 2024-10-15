## Spring Boot / Security OAuth2 Client ##

This is a general example of an OAuth2 client, which works with several different possible OAuth2 authorization servers. This example is based largely on https://spring.io/guides/tutorials/spring-boot-oauth2 / https://github.com/spring-guides/tut-spring-boot-oauth2.


It is first and foremost a standard Spring Boot web app. http://localhost:8080 will take you to a landing page that is publicly accessible. Login links are available for GitHub, Google, and a Spring Boot-based Authorization server.  However, these each require additional configuration to work.

* **GitHub**:  To use this with GitHub, you must establish an OAuth application on your GitHub account at https://github.com/settings/developers, being sure to set the Authorization callback URL to the value expected by this client: http://localhost:8080/login/oauth2/code/github.  Then you must set the resulting client-id and client-secret values as environment variables.   

* **Google**:  To use this with Google, you must establish a "project" with "credentials" in your Google account at https://console.cloud.google.com/apis, being sure to set the Authorization callback URL to the value expected by this client:  http://localhost:8080/login/oauth2/code/google.  Then you must set the resulting client-id and client-secret values as environment variables.  See instructions at https://developers.google.com/identity/openid-connect/openid-connect, starting at the section on "Setting up OAuth 2.0".

* **Spring Authorization Server**: To use this with a Spring-based Authorization server, follow the instructions at https://github.com/kennyk65/spring-teaching-demos/tree/master/oauth2AuthServer to run an auth server.  The settings in that project already align with the settings in this client project; simply run the server and the client together on localhost.

Spring Security allows public access to the root "/" resource containing the landing page.  All other resources require authentication.

This example does not add a resource server.