This is an example of a Spring Boot web app running over HTTPS.

The Spring Boot app itself is a very simple "hello world" style app.  `https://localhost:8443` will take
you to a hello world page.  The interesting content is in the application.properties where the KeyStore is identified.

The actual odd part is setting up the KeyStore.  To do this, find the keytool command - it is basically in the same spot that "java" is.  Then run this command, you will be prompted for a password and a BUNCH OF other stuff:

keytool -genkey -alias mydomain -keyalg RSA -keystore MyKeyStore.jks -keysize 2048

			Enter keystore password:  
			Re-enter new password: 
			What is your first and last name?
			  [Unknown]:  ken krueger
			What is the name of your organizational unit?
			  [Unknown]:  hilltop
			What is the name of your organization?
			  [Unknown]:  hilltop
			What is the name of your City or Locality?
			  [Unknown]:  Orlando
			What is the name of your State or Province?
			  [Unknown]:  FL
			What is the two-letter country code for this unit?
			  [Unknown]:  US
			Is CN=ken krueger, OU=hilltop, O=hilltop, L=Orlando, ST=FL, C=US correct?
			  [no]:  y
			
			Enter key password for <mydomain>
				(RETURN if same as keystore password):  

Then you can place the resulting "MyKeyStore.jks" file in the application itself, and refer to it via classpath.

When running the app, you will still be warned by your browser that the connection is unsafe.  This is because your browser does not have any way to vouch for the authenticity of the server without a certificate.  It is possible to make a self-signed certificate, but your browser will still warn you about that.  To eliminate this problem entirely, you need to get a certificate.s

