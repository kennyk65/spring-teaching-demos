# 
Simple Web App for demonstrating basic Spring Boot / Spring Security usage.  Adapted from the Spring Security lab from the Pivotal Core Spring course.  

The app is a simple Spring Boot web app with a home page and a 'secured' page.  Any attempt to get to the secured page will direct the user to a login page.  After login the secured page will be available.  Two login users are provided, "vince" and "edith", both with passwords that match the username.

Some basic integration tests are provided to ensure that unauthenticated access to secured resources results in a trip to the login page, and authenticated access is uneventful.

Build / Run:
This is a standard maven project that can be run with mvn clean package.  The resulting JAR file can be run with java -jar spring-security-simple-login-1.jar

Other features:
- Mustache is used for web pages
- Bootstrap is used for formatting.
