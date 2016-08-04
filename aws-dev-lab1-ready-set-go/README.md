# 
Simple Web App for demonstrating Blue / Green deployments.  Essentially identical to spring-cloud-aws-environment-demo.

See src/main/resources/templates/environment.html.  There are two DIVs at the top, one for blue, one for green.

Other features:
- Demonstrates basic usage of a Spring Cloud AWS web app
- Uses Spring Cloud AWS to obtain EC2 instance metadata when running in AWS.
- Behaves nicely when running outside of AWS
- Show actuator
- Thymeleaf is used for web pages
- Bootstrap is used for formatting.
