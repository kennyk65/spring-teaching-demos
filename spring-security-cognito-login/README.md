# 
Simple Web App for demonstrating basic Spring Boot / Spring Security usage, using AWS Cognito User Pool as the AuthenticationProvider.

The app is a simple Spring Boot web app with a home page and a 'secured' page.  Any attempt to get to the secured page will direct the user to a login page.  After login the secured page will be available.  

AWS Cognito is used to provide a "User Pool", which is a store of all application users.  To create this...

The security configuration in SecurityConfig.java is reasonably simple.  Anyone can reach the landing page (index.html) or the login page (login.html), but one must be authenticated to reach any "secured" URL like "secured/aSecuredPage.html".  /logoff is used to log out.  All authentication questions
are turned over to the CognitoAuthenticationProvider to interface with AWS Cognito.

Working with AWS Cognito is very complex and there are lots of options / possibilities.  This demo attempts to keep things simple by only using a basic User Pool with a very basic "authentication flow".

Some basic integration tests are provided to ensure that unauthenticated access to secured resources results in a trip to the login page, and authenticated access is uneventful.

Build / Run:
This is a standard maven project that can be run with mvn clean package.  The resulting JAR file can be run with java -jar spring-security-simple-login-1.jar

Other features:
- Mustache is used for web pages
- Bootstrap is used for formatting.
