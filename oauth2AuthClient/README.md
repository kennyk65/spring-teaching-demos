This is an example OAuth2 client web application.

It is first and foremost a standard Spring Boot web app.  `http://localhost:8080` will take
you to a landing page that is publicly accessible.  When going to other resources the
OAuth2 authentication/authorization kicks in.  Google, GitHub have been setup as OAuth2 providers.

A local OAuth2 server has been setup as an OAuth2 provider as well.

WHERE I LEFT OFF:
1. Unable to complete flow with local OAuth2 Auth Server.  Able to get to login page, and accept the desired scope, but presently stuck on UserInfo URL.  Not sure what scope to use.  
Not sure how to get JWT tokens.