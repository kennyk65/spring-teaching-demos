#FROM java:8
#FROM bitnami/java:11
FROM eclipse-temurin:17.0.5_8-jre-alpine

# Make a new directory and use it as work directory:
WORKDIR /opt/app

# Adding the file we just created in the build process as 'app.war'
COPY /target/spring-cloud-aws-environment-demo-1.war app.war

# Do not run as root.  Instead create a group and user.
# Change the ownership of WORKDIR so javauser can run from here
RUN addgroup --system javauser \
&& adduser -S -s /usr/sbin/nologin -G javauser javauser \
&& chown -R javauser:javauser .

# Change user to non-root and run:
USER javauser
ENTRYPOINT ["java", "-jar", "app.war"]
