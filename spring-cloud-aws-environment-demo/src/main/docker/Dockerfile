# This is my cool dockerfile
# See http://spring.io/guides/gs/spring-boot-docker/

FROM java:8
MAINTAINER Ken Krueger

# Optional, in case anything uses local disk.  Will be in var/lib/docker
VOLUME /tmp

# Adding the file we just created in the build process as 'app.jar'
ADD /target/spring-cloud-aws-environment-demo-1.war app.war

# Optional, adds a last modified time to each file.  Not needed here.
RUN bash -c 'touch /app.jar'

# Equivalent of running "java -jar /app.jar".
ENTRYPOINT ["java","-jar","/app.jar"]